package com.ll.biztrip.domain.travel.bus.dto;

import com.ll.biztrip.domain.travel.bus.entity.Terminal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Builder
@Getter
public class TerminalDto {

    private String terminalId;

    private String terminalName;

    public TerminalDto(Terminal terminal){
        this.terminalId = terminal.getTerminalId();
        this.terminalName = terminal.getTerminalName();
    }
}
