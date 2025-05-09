package com.ll.biztrip.domain.weather.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class DailySummaryDto {

    private String date;

    private String amSky;

    private String pmSky;

    private String amPrecipitation;

    private String pmPrecipitation;

    private String rainProb;

    private String maxTemp;

    private String minTemp;

}