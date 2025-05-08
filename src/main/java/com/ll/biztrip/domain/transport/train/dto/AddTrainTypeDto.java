package com.ll.biztrip.domain.transport.train.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddTrainTypeDto {
    @JsonProperty("vehiclekndid")
    private String trainId;

    @JsonProperty("vehiclekndnm")
    private String trainName;
}
