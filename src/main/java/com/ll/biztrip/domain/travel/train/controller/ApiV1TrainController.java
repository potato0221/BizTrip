package com.ll.biztrip.domain.travel.train.controller;


import com.ll.biztrip.domain.travel.train.service.TrainService;
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
@RequestMapping("/api/v1/travel/ktx")
@RequiredArgsConstructor
public class ApiV1TrainController {

    private final TrainService trainService;
    private final Rq rq;

    @PostMapping("/addCity")
    @Operation(summary = "Ktx 도시 등록")
    public RsData<Empty> addCities() {

        if(!rq.isAdmin()){
            System.out.println("권한 부족");
            return RsData.of(Msg.E403_0_FORBIDDEN.getCode(),Msg.E403_0_FORBIDDEN.getMsg());
        }

        trainService.updateKtxCity();

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
}
