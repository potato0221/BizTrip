package com.ll.biztrip.domain.trip.transport.bus.dto;


import com.ll.biztrip.domain.trip.transport.bus.entity.Bus;
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
public class BusRegisterDto {

    @NotBlank
    private String departureName;

    @NotBlank
    private String arrivalName;

    @NotNull
    private LocalDateTime departureTime;

    @NotNull
    private LocalDateTime arrivalTime;

    @NotBlank
    private String busGrade;

    public BusRegisterDto(Bus bus){
        this.departureName = bus.getDepartureName();
        this.arrivalName = bus.getArrivalName();
        this.departureTime = bus.getDepartureTime();
        this.arrivalTime = bus.getArrivalTime();
        this.busGrade = bus.getBusGrade();
    }
}
