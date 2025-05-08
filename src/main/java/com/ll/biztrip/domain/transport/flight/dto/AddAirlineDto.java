package com.ll.biztrip.domain.transport.flight.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddAirlineDto {
    @JsonProperty("airlineId")
    private String airlineId;

    @JsonProperty("airlineNm")
    private String airlineName;
}
