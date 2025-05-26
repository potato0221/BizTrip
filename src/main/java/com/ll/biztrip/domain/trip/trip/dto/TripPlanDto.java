package com.ll.biztrip.domain.trip.trip.dto;


import com.ll.biztrip.domain.trip.trip.entity.TripPlan;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PROTECTED;

@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Builder
@Getter
public class TripPlanDto {

    private Long id;

    @NotBlank
    private String startAddress;

    @NotBlank
    private String endAddress;

    @NotBlank
    private String planName;

    @Valid
    private List<TripLegDto> legs;

    public TripPlanDto(TripPlan tripPlan){
        this.id = tripPlan.getId();
        this.startAddress = tripPlan.getStartAddress();
        this.endAddress = tripPlan.getEndAddress();
        this.planName = tripPlan.getPlanName();
        this.legs = tripPlan.getLegs().stream()
                .map(TripLegDto::new)
                .collect(Collectors.toList());
    }
}
