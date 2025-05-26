package com.ll.biztrip.domain.trip.transport.train.controller;


import com.ll.biztrip.domain.member.member.entity.Member;
import com.ll.biztrip.domain.trip.transport.train.dto.*;
import com.ll.biztrip.domain.trip.transport.train.service.TrainService;
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
@RequestMapping("/api/v1/transport/train")
@RequiredArgsConstructor
public class ApiV1TrainController {

    private final TrainService trainService;
    private final Rq rq;

    @PostMapping("/addCity")
    @Operation(summary = "기차 도시 등록")
    @PreAuthorize("hasRole('ADMIN')")
    public RsData<Empty> addCities() {

        trainService.updateKtxCity();

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

    @PostMapping("/addTrainType")
    @Operation(summary = "기차 종류 등록")
    @PreAuthorize("hasRole('ADMIN')")
    public RsData<Empty> addTrainType() {

        trainService.updateTrainType();

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

    @PostMapping("/addStation")
    @Operation(summary = "기차역 등록")
    @PreAuthorize("hasRole('ADMIN')")
    public RsData<Empty> addStations() {

        trainService.updateStation();

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

    @GetMapping("/trainType")
    @Operation(summary = "기차 종류 조회")
    @PreAuthorize("isAuthenticated()")
    public RsData<List<TrainTypeDto>> getTrainType(){

        List<TrainTypeDto> requestDtos = trainService.getTrainTypes();

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), requestDtos);
    }

    @GetMapping("/stationList")
    @Operation(summary = "기차역 리스트 조회")
    @PreAuthorize("isAuthenticated()")
    public RsData<List<StationDto>> getStations(){

        List<StationDto> requestDtos = trainService.getStations();

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), requestDtos);
    }

    @GetMapping("/schedule")
    @Operation(summary = "날짜, 출발지, 도착지 별 기차 조회")
    @PreAuthorize("isAuthenticated()")
    public RsData<List<TrainScheduleDto>> getBusSchedule(
            @RequestParam String departureStationId,
            @RequestParam String arrivalStationId,
            @RequestParam String departureDate,
            @RequestParam String trainType){

        LocalDate parsedDate = LocalDate.parse(departureDate, DateTimeFormatter.ofPattern("yyyyMMdd"));

        List<TrainScheduleDto> scheduleDtos = trainService.getTrainSchedule(departureStationId, arrivalStationId, parsedDate, trainType);

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), scheduleDtos);
    }

    @PostMapping("/register")
    @Operation(summary = "내가 탑승 할 기차 등록")
    @PreAuthorize("isAuthenticated()")
    public RsData<Empty> addMyTrainSchedule(
            @RequestBody TrainRegisterDto trainRegisterDto
    ){

        Member member = rq.getMember();

        trainService.addTrainSchedule(trainRegisterDto, member);

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

    @GetMapping("/myList")
    @Operation(summary = "내가 탈 기차 리스트")
    @PreAuthorize("isAuthenticated()")
    public RsData<List<TrainDto>> getMyBuses(){

        List<TrainDto> trainDtos = trainService.getMyTrains(rq.getMember());

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), trainDtos);
    }
}
