package com.ll.biztrip.domain.transport.flight.controller;


import com.ll.biztrip.domain.member.member.entity.Member;
import com.ll.biztrip.domain.transport.flight.dto.AirlineDto;
import com.ll.biztrip.domain.transport.flight.dto.AirportDto;
import com.ll.biztrip.domain.transport.flight.dto.FlightRegisterDto;
import com.ll.biztrip.domain.transport.flight.dto.FlightScheduleDto;
import com.ll.biztrip.domain.transport.flight.service.FlightService;
import com.ll.biztrip.global.exceptions.GlobalException;
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
@RequestMapping("/api/v1/travel/flight")
@RequiredArgsConstructor
public class ApiV1FlightController {

    private final FlightService flightService;
    private final Rq rq;

    @PostMapping("/addAirport")
    @Operation(summary = "공항 등록")
    public RsData<Empty> addAirports() {

        if(!rq.isAdmin()){
            System.out.println("권한 부족");
            return RsData.of(Msg.E403_0_FORBIDDEN.getCode(),Msg.E403_0_FORBIDDEN.getMsg());
        }

        flightService.updateAirport();

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

    @PostMapping("/addAirline")
    @Operation(summary = "항공사 등록")
    public RsData<Empty> addAirlines() {

        if(!rq.isAdmin()){
            System.out.println("권한 부족");
            return RsData.of(Msg.E403_0_FORBIDDEN.getCode(),Msg.E403_0_FORBIDDEN.getMsg());
        }

        flightService.updateAirline();

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

    @GetMapping("/airline")
    @Operation(summary = "항공사 리스트 조회")
    public RsData<List<AirlineDto>> getAirline(){

        List<AirlineDto> requestDtos = flightService.getAirlines();

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), requestDtos);
    }

    @GetMapping("/airport")
    @Operation(summary = "공항 리스트 조회")
    public RsData<List<AirportDto>> getAirport(){

        List<AirportDto> requestDtos = flightService.getAirports();

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), requestDtos);
    }

    @GetMapping("/schedule")
    @Operation(summary = "날짜, 출발지, 도착지 별 항공편 조회")
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
    public RsData<Empty> addMyFlightSchedule(
            @RequestBody FlightRegisterDto flightRegisterDto
    ){
        if(rq.getMember()==null){
            throw new GlobalException(Msg.E401_0_UNAUTHORIZED.getCode(), Msg.E401_0_UNAUTHORIZED.getMsg());
        }else{
            Member member = rq.getMember();
            flightService.addFlightSchedule(flightRegisterDto, member);
        }

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

}
