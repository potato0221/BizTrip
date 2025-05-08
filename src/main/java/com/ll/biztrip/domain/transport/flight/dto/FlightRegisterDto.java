package com.ll.biztrip.domain.transport.flight.dto;


import com.ll.biztrip.domain.transport.flight.entity.Flight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Builder
@Getter
public class FlightRegisterDto {

    private String departureName;

    private String arrivalName;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;

    private String flightNumber;

    private String airline;

    public FlightRegisterDto(Flight flight){
        this.departureName = flight.getDepartureName();
        this.arrivalName = flight.getArrivalName();
        this.departureTime = flight.getDepartureTime();
        this.arrivalTime = flight.getArrivalTime();
        this.flightNumber = flight.getFlightNumber();
        this.airline = flight.getAirline();
    }
}
