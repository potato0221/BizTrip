package com.ll.biztrip.domain.travel.flight.controller;


import com.ll.biztrip.domain.travel.flight.service.FlightService;
import com.ll.biztrip.global.msg.Msg;
import com.ll.biztrip.global.rq.Rq;
import com.ll.biztrip.global.rsData.RsData;
import com.ll.biztrip.standard.base.Empty;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
