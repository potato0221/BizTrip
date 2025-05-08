package com.ll.biztrip.domain.transport.train.dto;

import com.ll.biztrip.domain.transport.train.entity.TrainType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Builder
@Getter
public class TrainTypeDto {

    private String trainId;

    private String trainName;

    public TrainTypeDto(TrainType trainType){
        this.trainId = trainType.getTrainId();
        this.trainName = trainType.getTrainName();
    }
}
