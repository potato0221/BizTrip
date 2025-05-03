package com.ll.biztrip.domain.travel.bus.controller;


import com.ll.biztrip.domain.travel.bus.dto.BusScheduleDto;
import com.ll.biztrip.domain.travel.bus.dto.TerminalDto;
import com.ll.biztrip.domain.travel.bus.service.BusService;
import com.ll.biztrip.global.msg.Msg;
import com.ll.biztrip.global.rq.Rq;
import com.ll.biztrip.global.rsData.RsData;
import com.ll.biztrip.standard.base.Empty;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/v1/travel/bus")
@RequiredArgsConstructor
public class ApiV1BusController {

    private final BusService busService;
    private final Rq rq;

    @PostMapping("/addTerminal")
    @Operation(summary = "버스 터미널 등록")
    public RsData<Empty> addTerminal() {

        if(!rq.isAdmin()){
            System.out.println("권한 부족");
            return RsData.of(Msg.E403_0_FORBIDDEN.getCode(),Msg.E403_0_FORBIDDEN.getMsg());
        }

        busService.updateTerminal();

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

    @GetMapping("/terminalList")
    @Operation(summary = "터미널 리스트 조회")
    public RsData<List<TerminalDto>> getTerminals(){

        List<TerminalDto> terminalDtos = busService.getTerminals();

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                terminalDtos);

    }

    @GetMapping("/schedule")
    @Operation(summary = "날짜, 출발지, 도착지 별 버스 조회")
    public RsData<List<BusScheduleDto>> getBusSchedule(
            @RequestParam String departureTerminalId,
            @RequestParam String arrivalTerminalId,
            @RequestParam String departureDate){

        LocalDate parsedDate = LocalDate.parse(departureDate, DateTimeFormatter.ofPattern("yyyyMMdd"));

        List<BusScheduleDto> scheduleDtos = busService.getBusSchedule(departureTerminalId, arrivalTerminalId, parsedDate);

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), scheduleDtos);
    }
}
