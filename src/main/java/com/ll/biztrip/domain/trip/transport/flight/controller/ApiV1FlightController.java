package com.ll.biztrip.domain.trip.transport.flight.controller;


import com.ll.biztrip.domain.member.member.entity.Member;
import com.ll.biztrip.domain.trip.transport.flight.dto.*;
import com.ll.biztrip.domain.trip.transport.flight.service.FlightService;
import com.ll.biztrip.global.enums.Msg;
import com.ll.biztrip.global.rq.Rq;
import com.ll.biztrip.global.rsData.RsData;
import com.ll.biztrip.standard.base.Empty;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transport/flight")
@RequiredArgsConstructor
public class ApiV1FlightController {

    private final FlightService flightService;
    private final Rq rq;

    @PostMapping("/addAirport")
    @Operation(summary = "공항 등록")
    @PreAuthorize("hasRole('ADMIN')")
    public RsData<Empty> addAirports() {

        flightService.updateAirport();

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

    @PostMapping("/addAirline")
    @Operation(summary = "항공사 등록")
    @PreAuthorize("hasRole('ADMIN')")
    public RsData<Empty> addAirlines() {

        flightService.updateAirline();

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

    @GetMapping("/airline")
    @Operation(summary = "항공사 리스트 조회")
    @PreAuthorize("isAuthenticated()")
    public RsData<List<AirlineDto>> getAirline(){

        List<AirlineDto> requestDtos = flightService.getAirlines();

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), requestDtos);
    }

    @GetMapping("/airport")
    @Operation(summary = "공항 리스트 조회")
    @PreAuthorize("isAuthenticated()")
    public RsData<List<AirportDto>> getAirport(){

        List<AirportDto> requestDtos = flightService.getAirports();

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), requestDtos);
    }

    @GetMapping("/schedule")
    @Operation(summary = "날짜, 출발지, 도착지 별 항공편 조회")
    @PreAuthorize("isAuthenticated()")
    public RsData<List<FlightScheduleDto>> getFlightSchedule(
            @RequestParam String departureAirportId,
            @RequestParam String arrivalAirportId,
            @RequestParam String departureDate,
            @RequestParam String airlineId){

        LocalDate parsedDate = LocalDate.parse(departureDate, DateTimeFormatter.ofPattern("yyyyMMdd"));

        List<FlightScheduleDto> scheduleDtos = flightService.getFlightSchedule(departureAirportId, arrivalAirportId, parsedDate, airlineId);

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), scheduleDtos);
    }

    @PostMapping("/register")
    @Operation(summary = "내가 탑승 할 항공편 등록")
    @PreAuthorize("isAuthenticated()")
    public RsData<Empty> addMyFlightSchedule(
            @Valid @RequestBody FlightRegisterDto flightRegisterDto
    ){

        Member member = rq.getMember();

        flightService.addFlightSchedule(flightRegisterDto, member);

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

    @GetMapping("/myList")
    @Operation(summary = "내가 탈 항공편 리스트")
    @PreAuthorize("isAuthenticated()")
    public RsData<List<FlightDto>> getMyBuses(){

        List<FlightDto> flightDtos = flightService.getMyFlights(rq.getMember());

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), flightDtos);
    }

}
