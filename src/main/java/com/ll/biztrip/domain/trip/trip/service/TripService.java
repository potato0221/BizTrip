package com.ll.biztrip.domain.trip.trip.service;


import com.ll.biztrip.domain.member.member.entity.Member;
import com.ll.biztrip.domain.trip.trip.dto.PlanListDto;
import com.ll.biztrip.domain.trip.trip.dto.TripLegDto;
import com.ll.biztrip.domain.trip.trip.dto.TripPlanDto;
import com.ll.biztrip.domain.trip.trip.entity.TripLeg;
import com.ll.biztrip.domain.trip.trip.entity.TripPlan;
import com.ll.biztrip.domain.trip.trip.repository.TripLegRepository;
import com.ll.biztrip.domain.trip.trip.repository.TripPlanRepository;
import com.ll.biztrip.domain.weather.weather.entity.LocationCodeMapping;
import com.ll.biztrip.domain.weather.weather.service.WeatherService;
import com.ll.biztrip.global.enums.Msg;
import com.ll.biztrip.global.enums.TransportType;
import com.ll.biztrip.global.exceptions.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TripService {

    private final TripPlanRepository tripPlanRepository;
    private final TripLegRepository tripLegRepository;
    private final WeatherService weatherService;

    @Transactional
    public void addTripPlan(TripPlanDto tripPlanDto, Member member){

        TripPlan tripPlan = TripPlan.builder()
                .startAddress(tripPlanDto.getStartAddress())
                .endAddress(tripPlanDto.getEndAddress())
                .planName(tripPlanDto.getPlanName())
                .member(member)
                .build();

        String locationCode = weatherService.findBestCode(tripPlanDto.getEndAddress());

        LocationCodeMapping locationCodeMapping = LocationCodeMapping.builder()
                .locationCode(locationCode)
                .tripPlan(tripPlan)
                .build();

        tripPlan.connectLocationMapping(locationCodeMapping);

        tripPlanRepository.save(tripPlan);
    }

    @Transactional
    public TripPlan addLeg(Long tripPlanId, TripLegDto tripLegDto) {

        TripPlan tripPlan = tripPlanRepository.findById(tripPlanId)
                .orElseThrow(()-> new GlobalException(
                        Msg.E404_0_DATA_NOT_FOUND.getCode(),
                        Msg.E404_0_DATA_NOT_FOUND.getMsg()));

        boolean exists = tripPlan.getLegs().stream().anyMatch(leg ->
                leg.getTransportType().name().equalsIgnoreCase(tripLegDto.getTransportType()) &&
                        leg.getTransportId().equals(tripLegDto.getTransportId())
        );

        if(exists){
            throw new GlobalException(
                    Msg.E400_4_ALREADY_REGISTERED_LEG.getCode(),
                    Msg.E400_4_ALREADY_REGISTERED_LEG.getMsg());
        }

        TripLeg tripLeg = TripLeg.builder()
                .tripPlan(tripPlan)
                .transportType(TransportType.valueOf(tripLegDto.getTransportType()))
                .transportId(tripLegDto.getTransportId())
                .arrivalName(tripLegDto.getArrivalName())
                .departureName(tripLegDto.getDepartureName())
                .departureTime(tripLegDto.getDepartureTime())
                .arrivalTime(tripLegDto.getArrivalTime())
                .build();

        tripLeg.setTripPlan(tripPlan);

        tripPlan.getLegs().add(tripLeg);

        return tripPlanRepository.save(tripPlan);
    }

    public List<PlanListDto> getPlanList(Member member) {

       return tripPlanRepository.findTripPlanByMember(member)
               .stream()
               .map(PlanListDto::new)
               .toList();

    }

    public List<TripPlanDto> getPlanDetail(Member member) {

        return tripPlanRepository.findTripPlanByMember(member)
                .stream()
                .map(TripPlanDto::new)
                .toList();
    }

    @Transactional
    public void deletePlan(Long planId, Member member) {

        tripPlanRepository.deleteByIdAndMember(planId, member);
    }

    @Transactional
    public void deleteLeg(Long legId, Member member) {

        tripLegRepository.deleteByIdAndTripPlanMember(legId, member);
    }

    public List<TripPlanDto> getTodayPlan() {

        return tripPlanRepository.findTodayPlans()
                .stream()
                .map(TripPlanDto::new)
                .toList();
    }
}
