package com.ll.biztrip.domain.weather.weather.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.biztrip.domain.weather.weather.dto.DailySummaryDto;
import com.ll.biztrip.domain.weather.weather.dto.WeatherDto;
import com.ll.biztrip.domain.weather.weather.dto.WeatherInfoDto;
import com.ll.biztrip.domain.weather.weather.dto.WeatherResponseDto;
import com.ll.biztrip.domain.weather.weather.entity.WeatherGridLocation;
import com.ll.biztrip.domain.weather.weather.repository.LocationCodeMappingRepository;
import com.ll.biztrip.domain.weather.weather.repository.WeatherGridLocationRepository;
import com.ll.biztrip.global.app.AppConfig;
import com.ll.biztrip.standard.util.AddressParser;
import com.ll.biztrip.standard.util.WeatherCodeTranslator;
import com.ll.biztrip.standard.util.WeatherTimeUtil;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WeatherService {

    private final WeatherGridLocationRepository weatherGridLocationRepository;
    private final LocationCodeMappingRepository locationCodeMappingRepository;

    @Transactional
    public void loadFromCsv() {

        if (weatherGridLocationRepository.count() > 0) {
            throw new IllegalStateException("이미 데이터가 존재합니다.");
        }

        String csvFilePath = AppConfig.getCsvFilePath();

        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {

            List<String[]> rows = reader.readAll();
            rows.remove(0);

            List<WeatherGridLocation> batch = new ArrayList<>();

            for (String[] row : rows) {
                WeatherGridLocation weatherGridLocation = WeatherGridLocation.builder()
                        .locationCode(row[1])
                        .siDo(row[2])
                        .siGunGu(row[3])
                        .dong(row[4])
                        .gridX(Integer.parseInt(row[5]))
                        .gridY(Integer.parseInt(row[6]))
                        .build();

                batch.add(weatherGridLocation);
            }

            weatherGridLocationRepository.saveAll(batch);

            System.out.println("총 저장된 건수: " + batch.size());

        } catch (IOException | CsvException e) {
            throw new RuntimeException("CSV 읽기 실패", e);
        }
    }

    public String findBestCode(String fullAddress) {

        AddressParser.ParsedAddress parsed = AddressParser.parse(fullAddress);
        System.out.println("▶ 시도: " + parsed.siDo());
        System.out.println("▶ 시군구: " + parsed.siGunGu());

        return weatherGridLocationRepository.
                findBestLocationCode(parsed.siDo(), parsed.siGunGu(), parsed.dong())
                .orElse(null);
    }

    public List<WeatherDto> getShortTermForecast(int nx, int ny, LocalDateTime now) {

        List<WeatherDto> weatherDtos = new ArrayList<>();

        try {
            WeatherTimeUtil.BaseDateTime base = WeatherTimeUtil.resolveBaseDateTime(now);
            String baseDate = base.getBaseDateStr();
            String baseTime = base.baseTime();

            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst");
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + AppConfig.openApiKey);
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=1");
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + 500);
            urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + baseDate);
            urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + baseTime);
            urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + nx);
            urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + ny);
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));


            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            BufferedReader br = (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300)
                    ? new BufferedReader(new InputStreamReader(conn.getInputStream()))
                    : new BufferedReader(new InputStreamReader(conn.getErrorStream()));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            br.close();
            conn.disconnect();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // ✨ 버스 방식 맞춤

            JsonNode itemNode = objectMapper.readTree(sb.toString())
                    .path("response").path("body").path("items").path("item");

            if (itemNode.isMissingNode() || !itemNode.isArray()) {
                return weatherDtos;
            }

            List<WeatherDto> schedules = Arrays.asList(objectMapper.treeToValue(itemNode, WeatherDto[].class));
            weatherDtos.addAll(schedules);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return weatherDtos;
    }

    public WeatherResponseDto getFormattedForecast(Long tripPlanId) {

        String locationCode = locationCodeMappingRepository.findByTripPlanId(tripPlanId).getLocationCode();

        WeatherGridLocation grid = weatherGridLocationRepository.findByLocationCode(locationCode);

        String localName = grid.getFullLocalName();

        List<WeatherDto> weatherDtos = getShortTermForecast(grid.getGridX(), grid.getGridY(), LocalDateTime.now());

        Map<String, WeatherInfoDto> hourlyMap = new TreeMap<>();
        Map<String, List<WeatherInfoDto>> groupedByDate = new LinkedHashMap<>();

        LocalDate today = LocalDate.now();
        LocalDate cutoff = today.plusDays(3);

        for (WeatherDto dto : weatherDtos) {
            String category = dto.getCategory();
            if (!List.of("SKY", "PTY", "TMP", "POP").contains(category)) continue;

            LocalDate date = LocalDate.parse(dto.getFcstDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
            if (date.isAfter(cutoff)) continue;

            String time = dto.getFcstTime().substring(0, 2) + ":00";
            String key = dto.getFcstDate() + dto.getFcstTime();

            WeatherInfoDto info = hourlyMap.getOrDefault(key, new WeatherInfoDto(date.toString(), time, null, null, null, null, localName));

            switch (category) {
                case "SKY" -> info.setSky(WeatherCodeTranslator.translateSKY(dto.getFcstValue()));
                case "PTY" -> info.setPrecipitation(WeatherCodeTranslator.translatePTY(dto.getFcstValue()));
                case "TMP" -> info.setTemperature(dto.getFcstValue());
                case "POP" -> info.setRainProb(dto.getFcstValue());
            }

            hourlyMap.put(key, info);
        }

        for (WeatherInfoDto info : hourlyMap.values()) {
            groupedByDate.computeIfAbsent(info.getDate(), k -> new ArrayList<>()).add(info);
        }

        List<DailySummaryDto> dailySummaries = new ArrayList<>();
        for (Map.Entry<String, List<WeatherInfoDto>> entry : groupedByDate.entrySet()) {
            String date = entry.getKey();
            LocalDate localDate = LocalDate.parse(date);
            if (localDate.isAfter(cutoff)) continue;

            List<WeatherInfoDto> infos = entry.getValue();

            String maxTemp = infos.stream().map(WeatherInfoDto::getTemperature).filter(Objects::nonNull).max(String::compareTo).orElse("-");
            String minTemp = infos.stream().map(WeatherInfoDto::getTemperature).filter(Objects::nonNull).min(String::compareTo).orElse("-");
            String rainProb = infos.stream().map(WeatherInfoDto::getRainProb).filter(Objects::nonNull).max(String::compareTo).orElse("-");

            List<WeatherInfoDto> amList = infos.stream()
                    .filter(i -> Integer.parseInt(i.getTime().substring(0, 2)) < 12)
                    .toList();
            List<WeatherInfoDto> pmList = infos.stream()
                    .filter(i -> Integer.parseInt(i.getTime().substring(0, 2)) >= 12)
                    .toList();

            String amSky = amList.isEmpty() ? "-" : amList.get(0).getSky();
            String pmSky = pmList.isEmpty() ? "-" : pmList.get(0).getSky();
            String amPty = amList.isEmpty() ? "-" : amList.get(0).getPrecipitation();
            String pmPty = pmList.isEmpty() ? "-" : pmList.get(0).getPrecipitation();

            dailySummaries.add(DailySummaryDto.builder()
                    .date(date)
                    .amSky(amSky)
                    .pmSky(pmSky)
                    .amPrecipitation(amPty)
                    .pmPrecipitation(pmPty)
                    .rainProb(rainProb)
                    .maxTemp(maxTemp)
                    .minTemp(minTemp)
                    .build());
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = now.plusHours(24);

        List<WeatherInfoDto> hourly = hourlyMap.values().stream()
                .filter(info -> {
                    LocalDateTime target = LocalDateTime.parse(info.getDate() + "T" + info.getTime());
                    return !target.isBefore(now) && !target.isAfter(end);
                })
                .collect(Collectors.toList());

        return WeatherResponseDto.builder()
                .hourly(hourly)
                .daily(dailySummaries)
                .build();
    }
}
