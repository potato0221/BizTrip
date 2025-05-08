package com.ll.biztrip.domain.transport.train.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainScheduleDto {

    @JsonProperty("depplandtime")
    private String departureTime;

    @JsonProperty("arrplandtime")
    private String arrivalTime;

    @JsonProperty("depplacename")
    private String departure;

    @JsonProperty("arrplacename")
    private String arrival;

    @JsonProperty("traingradename")
    private String trainType;
}

