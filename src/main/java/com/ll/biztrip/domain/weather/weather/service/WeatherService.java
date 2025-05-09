package com.ll.biztrip.domain.weather.weather.service;

import com.ll.biztrip.domain.weather.weather.entity.WeatherGridLocation;
import com.ll.biztrip.domain.weather.weather.repository.WeatherGridLocationRepository;
import com.ll.biztrip.global.app.AppConfig;
import com.ll.biztrip.standard.util.AddressParser;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WeatherService {

    private final WeatherGridLocationRepository weatherGridLocationRepository;

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
                findBestLocationCode(parsed.siDo(), parsed.siGunGu())
                .orElse(null);
    }
}
