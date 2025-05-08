package com.ll.biztrip.domain.trip.transport.train.controller;


import com.ll.biztrip.domain.member.member.entity.Member;
import com.ll.biztrip.domain.trip.transport.train.dto.StationDto;
import com.ll.biztrip.domain.trip.transport.train.dto.TrainRegisterDto;
import com.ll.biztrip.domain.trip.transport.train.dto.TrainScheduleDto;
import com.ll.biztrip.domain.trip.transport.train.dto.TrainTypeDto;
import com.ll.biztrip.domain.trip.transport.train.service.TrainService;
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
@RequestMapping("/api/v1/travel/train")
@RequiredArgsConstructor
public class ApiV1TrainController {

    private final TrainService trainService;
    private final Rq rq;

    @PostMapping("/addCity")
    @Operation(summary = "기차 도시 등록")
    public RsData<Empty> addCities() {

        if(!rq.isAdmin()){
            System.out.println("권한 부족");
            return RsData.of(Msg.E403_0_FORBIDDEN.getCode(),Msg.E403_0_FORBIDDEN.getMsg());
        }

        trainService.updateKtxCity();

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

    @PostMapping("/addTrainType")
    @Operation(summary = "기차 종류 등록")
    public RsData<Empty> addTrainType() {

        if(!rq.isAdmin()){
            System.out.println("권한 부족");
            return RsData.of(Msg.E403_0_FORBIDDEN.getCode(),Msg.E403_0_FORBIDDEN.getMsg());
        }

        trainService.updateTrainType();

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

    @PostMapping("/addStation")
    @Operation(summary = "기차역 등록")
    public RsData<Empty> addStations() {

        if(!rq.isAdmin()){
            System.out.println("권한 부족");
            return RsData.of(Msg.E403_0_FORBIDDEN.getCode(),Msg.E403_0_FORBIDDEN.getMsg());
        }

        trainService.updateStation();

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }

    @GetMapping("/trainType")
    @Operation(summary = "기차 종류 조회")
    public RsData<List<TrainTypeDto>> getTrainType(){

        List<TrainTypeDto> requestDtos = trainService.getTrainTypes();

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), requestDtos);
    }

    @GetMapping("/stationList")
    @Operation(summary = "기차역 리스트 조회")
    public RsData<List<StationDto>> getStations(){

        List<StationDto> requestDtos = trainService.getStations();

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), requestDtos);
    }

    @GetMapping("/schedule")
    @Operation(summary = "날짜, 출발지, 도착지 별 기차 조회")
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
    public RsData<Empty> addMyTrainSchedule(
            @RequestBody TrainRegisterDto trainRegisterDto
    ){
        if(rq.getMember()==null){
            throw new GlobalException(Msg.E401_0_UNAUTHORIZED.getCode(), Msg.E401_0_UNAUTHORIZED.getMsg());
        }else{
            Member member = rq.getMember();
            trainService.addTrainSchedule(trainRegisterDto, member);
        }

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());
    }
}
