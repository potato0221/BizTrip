package com.ll.biztrip.domain.trip.transport.flight.dto;


import com.ll.biztrip.domain.trip.transport.flight.entity.Flight;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    private String departureName;

    @NotBlank
    private String arrivalName;

    @NotNull
    private LocalDateTime departureTime;

    @NotNull
    private LocalDateTime arrivalTime;

    @NotBlank
    private String flightNumber;

    @NotBlank
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
