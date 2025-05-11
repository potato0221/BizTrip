package com.ll.biztrip.domain.weather.weather.repository;

import java.util.Optional;

public interface CustomWeatherGridLocationRepository {
    Optional<String> findBestLocationCode(String siDo, String siGunGu, String dong);

}
