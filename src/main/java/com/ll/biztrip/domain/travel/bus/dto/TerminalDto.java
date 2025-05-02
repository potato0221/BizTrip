package com.ll.biztrip.domain.travel.bus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerminalDto {

    @JsonProperty("terminalId")
    private String terminalId;

    @JsonProperty("terminalNm")
    private String terminalName;
}
