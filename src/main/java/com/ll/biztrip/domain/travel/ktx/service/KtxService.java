package com.ll.biztrip.domain.travel.ktx.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.biztrip.domain.travel.ktx.dto.CityDto;
import com.ll.biztrip.domain.travel.ktx.dto.StationDto;
import com.ll.biztrip.domain.travel.ktx.entity.City;
import com.ll.biztrip.domain.travel.ktx.entity.Station;
import com.ll.biztrip.domain.travel.ktx.repository.CityRepository;
import com.ll.biztrip.domain.travel.ktx.repository.StationRepository;
import com.ll.biztrip.global.app.AppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KtxService {

    private final CityRepository cityRepository;
    private final StationRepository stationRepository;

    public void updateCity() {
        if (cityRepository.count() > 0) {
            System.out.println("이미 데이터가 존재합니다. 요청을 취소 합니다.");
            return;
        }

        try {
            for (int i = 1; i <= 1; i++) {
                // 1. URL 설정
                StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1613000/TrainInfoService/getCtyCodeList");
                // 2. 오픈 API의요청 규격에 맞는 파라미터 생성, 발급받은 인증키.
                urlBuilder.append("?" + URLEncoder.encode("page", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(i), "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("perPage", "UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + AppConfig.openApiKeyKtx);
                urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
                // 3. URL 객체 생성.
                System.out.println("URL 요청 확인: " + urlBuilder.toString());

                URL url = new URL(urlBuilder.toString());
                // 4. 요청하고자 하는 URL과 통신하기 위한 Connection 객체 생성.
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 5. 통신을 위한 메소드 SET.
                conn.setRequestMethod("GET");
                // 6. 통신을 위한 Content-type SET.
                conn.setRequestProperty("Content-type", "application/json");
                // 7. 통신 응답 코드 확인.
                System.out.print(i + "번째 통신 결과 : ");
                if (conn.getResponseCode() == 200) {
                    System.out.println("성공");
                } else {
                    System.out.println("실패");
                }
                // 8. 전달받은 데이터를 BufferedReader 객체로 저장.
                BufferedReader br;
                if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                // 9. 저장된 데이터를 라인별로 읽어 StringBuilder 객체로 저장.
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                // 10. 객체 해제.
                br.close();
                conn.disconnect();

                ObjectMapper objectMapper = new ObjectMapper();

                // String -> JSON 변환
                JsonNode node1 = objectMapper.readTree(sb.toString());
                // JSON 안의 'data' 객체 추출
                JsonNode node2 = node1.path("response").path("body").path("items").path("item");

                if (node2.isMissingNode() || !node2.isArray()) {
                    System.out.println("데이터가 존재하지 않습니다.");
                    return;
                }

                // JSON엔 존재하나 DTO에는 존재하지 않는 매핑 값에 대해 처리
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                // DTO List 생성
                List<CityDto> cityDtos = Arrays.asList(objectMapper.treeToValue(node2, CityDto[].class));

                saveCities(cityDtos);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void saveCities(List<CityDto> cityDtos) {

        for(CityDto cityDto : cityDtos){

            if(cityRepository.existsByCityCode(cityDto.getCityCode())){
                continue;
            }

            City city = City.builder()
                    .cityCode(cityDto.getCityCode())
                    .cityName(cityDto.getCityName())
                    .build();

            cityRepository.save(city);
            System.out.println("저장");
        }
    }

    public void updateStation() {
        if (stationRepository.count() > 0) {
            System.out.println("이미 데이터가 존재합니다. 요청을 취소 합니다.");
            return;
        }

        try {
            if(cityRepository.count()==0){
                System.out.println("도시를 먼저 저장 해 주세요");
                return;
            }

            List<City> cities = cityRepository.findAll();

            for(City city : cities) {

                String cityCode = city.getCityCode();

                for (int i = 1; i <= 1; i++) {
                    StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1613000/TrainInfoService/getCtyAcctoTrainSttnList");
                    urlBuilder.append("?" + URLEncoder.encode("page", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(i), "UTF-8"));
                    urlBuilder.append("&" + URLEncoder.encode("perPage", "UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8"));
                    urlBuilder.append("&" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + AppConfig.openApiKeyKtx);
                    urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
                    urlBuilder.append("&" + URLEncoder.encode("cityCode", "UTF-8") + "=" + URLEncoder.encode(cityCode, "UTF-8"));

                    // 3. URL 객체 생성.
                    System.out.println("URL 요청 확인: " + urlBuilder.toString());

                    URL url = new URL(urlBuilder.toString());
                    // 4. 요청하고자 하는 URL과 통신하기 위한 Connection 객체 생성.
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    // 5. 통신을 위한 메소드 SET.
                    conn.setRequestMethod("GET");
                    // 6. 통신을 위한 Content-type SET.
                    conn.setRequestProperty("Content-type", "application/json");
                    // 7. 통신 응답 코드 확인.
                    System.out.print(i + "번째 통신 결과 : ");
                    if (conn.getResponseCode() == 200) {
                        System.out.println("성공");
                    } else {
                        System.out.println("실패");
                    }
                    // 8. 전달받은 데이터를 BufferedReader 객체로 저장.
                    BufferedReader br;
                    if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                        br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    } else {
                        br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    }
                    // 9. 저장된 데이터를 라인별로 읽어 StringBuilder 객체로 저장.
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    // 10. 객체 해제.
                    br.close();
                    conn.disconnect();

                    ObjectMapper objectMapper = new ObjectMapper();

                    // String -> JSON 변환
                    JsonNode node1 = objectMapper.readTree(sb.toString());
                    // JSON 안의 'data' 객체 추출
                    JsonNode node2 = node1.path("response").path("body").path("items").path("item");

                    if (node2.isMissingNode() || !node2.isArray()) {
                        System.out.println("데이터가 존재하지 않습니다.");
                        return;
                    }

                    // JSON엔 존재하나 DTO에는 존재하지 않는 매핑 값에 대해 처리
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    // DTO List 생성
                    List<StationDto> stationDtos = Arrays.asList(objectMapper.treeToValue(node2, StationDto[].class));

                    saveStations(stationDtos, city);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void saveStations(List<StationDto> stationDtos, City city) {

        for(StationDto stationDto : stationDtos){

            if(stationRepository.existsByStationId(stationDto.getStationId())){
                continue;
            }

            Station station = Station.builder()
                    .stationId(stationDto.getStationId())
                    .stationName(stationDto.getStationName()+"역")
                    .cityCode(city)
                    .build();

            stationRepository.save(station);
            System.out.println("저장");
        }
    }
}
