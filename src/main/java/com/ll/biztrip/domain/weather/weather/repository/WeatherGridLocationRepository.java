package com.ll.biztrip.domain.weather.weather.repository;

import com.ll.biztrip.domain.weather.weather.entity.WeatherGridLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherGridLocationRepository extends JpaRepository<WeatherGridLocation, Long> {
}
