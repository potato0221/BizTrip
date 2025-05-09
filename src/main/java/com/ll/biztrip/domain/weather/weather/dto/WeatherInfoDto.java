package com.ll.biztrip.domain.weather.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherInfoDto {

    private String date;

    private String time;

    private String sky;

    private String precipitation;

    private String temperature;

    private String rainProb;

    private String localName;

}
