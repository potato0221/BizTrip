package com.ll.biztrip.domain.transport.train.dto;

import com.ll.biztrip.domain.transport.train.entity.Station;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Builder
@Getter
public class StationDto {

    private String stationId;

    private String stationName;

    private String cityName;

    public StationDto(Station station){
        this.stationId = station.getStationId();
        this.stationName = station.getStationName();
        this.cityName = station.getTrainCity().getCityName();
    }
}
