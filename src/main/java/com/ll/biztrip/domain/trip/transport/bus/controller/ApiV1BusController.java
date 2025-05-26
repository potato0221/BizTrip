package com.ll.biztrip.domain.trip.transport.bus.controller;


import com.ll.biztrip.domain.member.member.entity.Member;
import com.ll.biztrip.domain.trip.transport.bus.dto.BusDto;
import com.ll.biztrip.domain.trip.transport.bus.dto.BusRegisterDto;
import com.ll.biztrip.domain.trip.transport.bus.dto.BusScheduleDto;
import com.ll.biztrip.domain.trip.transport.bus.dto.TerminalDto;
import com.ll.biztrip.domain.trip.transport.bus.service.BusService;
import com.ll.biztrip.global.enums.Msg;
import com.ll.biztrip.global.rq.Rq;
import com.ll.biztrip.global.rsData.RsData;
import com.ll.biztrip.standard.base.Empty;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public RsData<Empty> addTerminal() {

        busService.updateTerminal();

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

    @GetMapping("/terminalList")
    @Operation(summary = "터미널 리스트 조회")
    @PreAuthorize("isAuthenticated()")
    public RsData<List<TerminalDto>> getTerminals(){

        List<TerminalDto> terminalDtos = busService.getTerminals();

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                terminalDtos);

    }

    @GetMapping("/schedule")
    @Operation(summary = "날짜, 출발지, 도착지 별 버스 조회")
    @PreAuthorize("isAuthenticated()")
    public RsData<List<BusScheduleDto>> getBusSchedule(
            @RequestParam String departureTerminalId,
            @RequestParam String arrivalTerminalId,
            @RequestParam String departureDate){

        LocalDate parsedDate = LocalDate.parse(departureDate, DateTimeFormatter.ofPattern("yyyyMMdd"));

        List<BusScheduleDto> scheduleDtos = busService.getBusSchedule(departureTerminalId, arrivalTerminalId, parsedDate);

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), scheduleDtos);
    }

    @PostMapping("/register")
    @Operation(summary = "내가 탑승 할 버스 등록")
    @PreAuthorize("isAuthenticated()")
    public RsData<Empty> addMyBusSchedule(
            @RequestBody BusRegisterDto busRegisterDto
    ){

        Member member = rq.getMember();

        busService.addBusSchedule(busRegisterDto, member);

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

    @GetMapping("/myList")
    @Operation(summary = "내가 탈 버스 리스트")
    @PreAuthorize("isAuthenticated()")
    public RsData<List<BusDto>> getMyBuses(){

        List<BusDto> busDtos = busService.getMyBuses(rq.getMember());

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), busDtos);
    }
}
