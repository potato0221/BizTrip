package com.ll.biztrip.domain.trip.trip.dto;

import com.ll.biztrip.domain.trip.trip.entity.TripLeg;
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
public class TripLegDto {

    private Long id;

    @NotBlank
    private String transportType;

    @NotNull
    private Long transportId;

    @NotBlank
    private String departureName;

    @NotBlank
    private String arrivalName;

    @NotNull
    private LocalDateTime departureTime;

    @NotNull
    private LocalDateTime arrivalTime;


    public TripLegDto(TripLeg tripLeg){
        this.id = tripLeg.getId();
        this.transportType = tripLeg.getTransportType().name();
        this.transportId = tripLeg.getTransportId();
        this.departureName = tripLeg.getDepartureName();
        this.arrivalName = tripLeg.getArrivalName();
        this.departureTime = tripLeg.getDepartureTime();
        this.arrivalTime = tripLeg.getArrivalTime();
    }
}
