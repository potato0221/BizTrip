package com.ll.biztrip.domain.travel.ktx.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.biztrip.domain.travel.ktx.dto.CityDto;
import com.ll.biztrip.domain.travel.ktx.entity.City;
import com.ll.biztrip.domain.travel.ktx.repository.CityRepository;
import com.ll.biztrip.global.app.AppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KtxService {

    private final CityRepository cityRepository;
    private final ObjectMapper objectMapper;
    private final String CHARSET = "UTF-8";
    private final String serviceKey = AppConfig.getOpenApiKeyKtx();

    public void saveCities() {
        // 데이터 있을 시 종료
        if (cityRepository.count() > 0) {
            return;
        }

        try {

            String BASE_URL_CITY = "http://apis.data.go.kr/1613000/TrainInfoService/getCtyCodeList";

            if (serviceKey == null || serviceKey.isEmpty()) {
                throw new IllegalStateException("서비스 키를 확인 하세요");
            }

            String requestUrl = BASE_URL_CITY +
                    "?serviceKey=" + URLEncoder.encode(serviceKey, CHARSET) +
                    "&_type=json";

            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    conn.getResponseCode() >= 200 && conn.getResponseCode() < 300
                            ? conn.getInputStream()
                            : conn.getErrorStream(), CHARSET))) {

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }

                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                JsonNode itemsNode = objectMapper.readTree(response.toString())
                        .path("response").path("body").path("items").path("item");

                CityDto[] cityDtoArray = objectMapper.treeToValue(itemsNode, CityDto[].class);

                for (CityDto dto : cityDtoArray) {
                    City city = City.builder()
                            .cityCode(dto.getCityCode())
                            .cityName(dto.getCityName())
                            .build();

                    cityRepository.save(city);
                }

                System.out.println("✅ 도시 저장 완료\n");
            }

            conn.disconnect();
        } catch (Exception e) {
            throw new RuntimeException("도시 코드 API 호출 실패: " + e.getMessage(), e);
        }
    }

}
