package com.ll.biztrip.domain.travel.bus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusScheduleDto {

    @JsonProperty("depPlandTime")
    private String departureTime;

    @JsonProperty("arrPlandTime")
    private String arrivalTime;

    @JsonProperty("depPlaceNm")
    private String departure;

    @JsonProperty("arrPlaceNm")
    private String arrival;

    @JsonProperty("gradeNm")
    private String busGrade;
}

