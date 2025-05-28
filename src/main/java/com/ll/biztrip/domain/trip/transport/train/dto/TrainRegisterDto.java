package com.ll.biztrip.domain.trip.transport.train.dto;


import com.ll.biztrip.domain.trip.transport.train.entity.Train;
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
public class TrainRegisterDto {

    @NotBlank
    private String departureName;

    @NotBlank
    private String arrivalName;

    @NotNull
    private LocalDateTime departureTime;

    @NotNull
    private LocalDateTime arrivalTime;

    @NotBlank
    private String trainType;

    public TrainRegisterDto(Train train){
        this.departureName = train.getDepartureName();
        this.arrivalName = train.getArrivalName();
        this.departureTime = train.getDepartureTime();
        this.arrivalTime = train.getArrivalTime();
        this.trainType = train.getTrainType();
    }
}
