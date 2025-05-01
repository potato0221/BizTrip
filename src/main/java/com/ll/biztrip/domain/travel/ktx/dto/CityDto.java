package com.ll.biztrip.domain.travel.ktx.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityDto {
    @JsonProperty("citycode")
    private String cityCode;

    @JsonProperty("cityname")
    private String cityName;
}
