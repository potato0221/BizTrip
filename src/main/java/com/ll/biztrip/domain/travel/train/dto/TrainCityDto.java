package com.ll.biztrip.domain.travel.train.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainCityDto {
    @JsonProperty("citycode")
    private String cityCode;

    @JsonProperty("cityname")
    private String cityName;
}
