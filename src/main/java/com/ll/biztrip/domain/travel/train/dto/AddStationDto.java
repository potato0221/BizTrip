package com.ll.biztrip.domain.travel.train.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddStationDto {
    @JsonProperty("nodeid")
    private String stationId;

    @JsonProperty("nodename")
    private String stationName;
}
