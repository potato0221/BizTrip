package com.ll.biztrip.domain.transport.flight.dto;

import com.ll.biztrip.domain.transport.flight.entity.Airport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Builder
@Getter
public class AirportDto {

    private String airportId;

    private String airportName;

    public AirportDto(Airport airport){
        this.airportId = airport.getAirportId();
        this.airportName = airport.getAirportName();
    }
}
