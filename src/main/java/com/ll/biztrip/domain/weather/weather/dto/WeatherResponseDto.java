package com.ll.biztrip.domain.weather.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class WeatherResponseDto {

    private List<WeatherInfoDto> hourly;

    private List<DailySummaryDto> daily;

}
