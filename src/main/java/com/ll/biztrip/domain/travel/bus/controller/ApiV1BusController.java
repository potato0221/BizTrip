package com.ll.biztrip.domain.travel.bus.controller;


import com.ll.biztrip.domain.travel.bus.service.BusService;
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
}
