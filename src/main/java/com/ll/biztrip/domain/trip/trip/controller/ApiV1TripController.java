package com.ll.biztrip.domain.trip.trip.controller;


import com.ll.biztrip.domain.member.member.entity.Member;
import com.ll.biztrip.domain.trip.trip.dto.PlanListDto;
import com.ll.biztrip.domain.trip.trip.dto.TripLegDto;
import com.ll.biztrip.domain.trip.trip.dto.TripPlanDto;
import com.ll.biztrip.domain.trip.trip.entity.TripPlan;
import com.ll.biztrip.domain.trip.trip.service.TripService;
import com.ll.biztrip.global.enums.Msg;
import com.ll.biztrip.global.exceptions.GlobalException;
import com.ll.biztrip.global.rq.Rq;
import com.ll.biztrip.global.rsData.RsData;
import com.ll.biztrip.standard.base.Empty;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trip")
@RequiredArgsConstructor
public class ApiV1TripController {

    private final TripService tripService;
    private final Rq rq;

    @PostMapping("/addTripPlan")
    @Operation(summary = "여행 계획 추가")
    public RsData<Empty> addTripPlan(@RequestBody TripPlanDto tripPlanDto){

        if(rq.getMember()==null){
            throw new GlobalException(Msg.E401_0_UNAUTHORIZED.getCode(), Msg.E401_0_UNAUTHORIZED.getMsg());
        }

        Member member = rq.getMember();
        tripService.addTripPlan(tripPlanDto, member);

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg());

    }

    @PostMapping("/{tripPlanId}/addLeg")
    @Operation(summary = "여행 계획에 탑승 할 교통수단 추가")
    public RsData<TripPlanDto> addTripLeg(
            @PathVariable Long tripPlanId,
            @RequestBody TripLegDto tripLegDto){

        TripPlan updatedPlan = tripService.addLeg(tripPlanId, tripLegDto);

        return RsData.of(Msg.E200_0_CREATE_SUCCEED.getCode(), Msg.E200_0_CREATE_SUCCEED.getMsg(),
                new TripPlanDto(updatedPlan));
    }

    @GetMapping("/planList")
    @Operation(summary = "등록된 플랜 리스트 조회")
    public RsData<List<PlanListDto>> getPlanLists(){

        if(rq.getMember()==null){
            throw new GlobalException(Msg.E401_0_UNAUTHORIZED.getCode(), Msg.E401_0_UNAUTHORIZED.getMsg());
        }

        List<PlanListDto> planList = tripService.getPlanList(rq.getMember());

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), planList);
    }

    @GetMapping("/getPlan")
    @Operation(summary = "플랜 상세 정보")
    public RsData<List<TripPlanDto>> getPlanDetail(){

        if(rq.getMember()==null){
            throw new GlobalException(Msg.E401_0_UNAUTHORIZED.getCode(), Msg.E401_0_UNAUTHORIZED.getMsg());
        }

        List<TripPlanDto> planDtos = tripService.getPlanDetail(rq.getMember());

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), planDtos);
    }

    @DeleteMapping("/delete/plan/{planId}")
    @Operation(summary = "내 플랜 삭제")
    public RsData<Empty> deletePlan(
            @PathVariable Long planId
    ){

        tripService.deletePlan(planId, rq.getMember());

        return RsData.of(Msg.E200_3_DELETE_SUCCEED.getCode(), Msg.E200_3_DELETE_SUCCEED.getMsg());

    }

    @DeleteMapping("/delete/leg/{legId}")
    @Operation(summary = "내 교통편 삭제")
    public RsData<Empty> deleteLeg(
            @PathVariable Long legId
    ){

        tripService.deleteLeg(legId, rq.getMember());

        return RsData.of(Msg.E200_3_DELETE_SUCCEED.getCode(), Msg.E200_3_DELETE_SUCCEED.getMsg());
    }

    @GetMapping("/today")
    @Operation(summary = "오늘의 출장")
    public RsData<List<TripPlanDto>> getTodayPlan(){

        List<TripPlanDto> tripPlanDtos = tripService.getTodayPlan();

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(), Msg.E200_1_INQUIRY_SUCCEED.getMsg(), tripPlanDtos);
    }
}
