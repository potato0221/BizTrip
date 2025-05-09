package com.ll.biztrip.domain.weather.weather.dto;

import lombok.Data;

@Data
public class WeatherDto {

    private String fcstDate;

    private String fcstTime;

    private String category;

    private String fcstValue;

}
