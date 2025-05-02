package com.ll.biztrip.domain.travel.flight.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirportDto {
    @JsonProperty("airportId")
    private String airportId;

    @JsonProperty("airportNm")
    private String airportName;
}
