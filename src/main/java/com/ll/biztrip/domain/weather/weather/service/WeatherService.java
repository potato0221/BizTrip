package com.ll.biztrip.domain.weather.weather.service;

import com.ll.biztrip.domain.weather.weather.entity.WeatherGridLocation;
import com.ll.biztrip.domain.weather.weather.repository.WeatherGridLocationRepository;
import com.ll.biztrip.global.app.AppConfig;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WeatherService {

    private final WeatherGridLocationRepository weatherGridLocationRepository;

    public void loadFromCsv() {

        if (weatherGridLocationRepository.count() > 0) {
            throw new IllegalStateException("이미 데이터가 존재합니다.");
        }

        String csvFilePath = AppConfig.getCsvFilePath();

        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {

            List<String[]> rows = reader.readAll();

            rows.remove(0);

            for (String[] row : rows) {
                String locationCode = row[1];
                String siDo = row[2];
                String siGunGu = row[3];
                String dong = row[4];
                int gridX = Integer.parseInt(row[5]);
                int gridY = Integer.parseInt(row[6]);

                WeatherGridLocation weatherGridLocation = WeatherGridLocation.builder()
                        .locationCode(locationCode)
                        .siDo(siDo)
                        .siGunGu(siGunGu)
                        .dong(dong)
                        .gridX(gridX)
                        .gridY(gridY)
                        .build();

                weatherGridLocationRepository.save(weatherGridLocation);
            }

        } catch (IOException | CsvException e) {
            throw new RuntimeException("CSV 읽기 실패", e);
        }
    }
}
