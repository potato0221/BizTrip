package com.ll.biztrip.domain.travel.flight.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightScheduleDto {

    @JsonProperty("depPlandTime")
    private String departureTime;

    @JsonProperty("arrPlandTime")
    private String arrivalTime;

    @JsonProperty("depAirportNm")
    private String departure;

    @JsonProperty("arrAirportNm")
    private String arrival;

    @JsonProperty("airlineNm")
    private String busGrade;

    @JsonProperty("vihicleId")
    private String flightNumber;
}

