package com.ll.biztrip.domain.travel.flight.dto;

import com.ll.biztrip.domain.travel.flight.entity.Airline;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Builder
@Getter
public class AirlineDto {

    private String airlineId;

    private String airlineName;

    public AirlineDto(Airline airline){
        this.airlineId = airline.getAirlineId();
        this.airlineName = airline.getAirlineName();
    }
}
