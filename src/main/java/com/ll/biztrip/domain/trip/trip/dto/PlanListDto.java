package com.ll.biztrip.domain.trip.trip.dto;

import com.ll.biztrip.domain.trip.trip.entity.TripPlan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Builder
@Getter
public class PlanListDto {

    private String planName;

    private Long planId;

    public PlanListDto(TripPlan tripPlan){
        this.planId = tripPlan.getId();
        this.planName = tripPlan.getPlanName();
    }
}
