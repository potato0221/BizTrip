package com.ll.biztrip.domain.trip.transport.bus.dto;


import com.ll.biztrip.domain.trip.transport.bus.entity.Bus;
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
public class BusDto {

    private Long id;

    private String departureName;

    private String arrivalName;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;

    private String busGrade;

    public BusDto(Bus bus){
        this.id = bus.getId();
        this.departureName = bus.getDepartureName();
        this.arrivalName = bus.getArrivalName();
        this.departureTime = bus.getDepartureTime();
        this.arrivalTime = bus.getArrivalTime();
        this.busGrade = bus.getBusGrade();
    }
}
