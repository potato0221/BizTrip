package com.ll.biztrip.domain.trip.trip.dto;

import com.ll.biztrip.domain.trip.trip.entity.TripLeg;
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
public class TripLegDto {

    private String transportType;

    private Long transportId;

    private String departureName;

    private String arrivalName;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;


    public TripLegDto(TripLeg tripLeg){
        this.transportType = tripLeg.getTransportType().name();
        this.transportId = tripLeg.getTransportId();
        this.departureName = tripLeg.getDepartureName();
        this.arrivalName = tripLeg.getArrivalName();
        this.departureTime = tripLeg.getDepartureTime();
        this.arrivalTime = tripLeg.getArrivalTime();
    }
}
