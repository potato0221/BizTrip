package com.ll.biztrip.domain.trip.transport.train.dto;


import com.ll.biztrip.domain.trip.transport.train.entity.Train;
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
public class TrainDto {

    private Long id;

    private String departureName;

    private String arrivalName;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;

    private String trainType;

    public TrainDto(Train train){
        this.id = train.getId();
        this.departureName = train.getDepartureName();
        this.arrivalName = train.getArrivalName();
        this.departureTime = train.getDepartureTime();
        this.arrivalTime = train.getArrivalTime();
        this.trainType = train.getTrainType();
    }
}
