package com.ll.biztrip.domain.trip.trip.service;


import com.ll.biztrip.domain.member.member.entity.Member;
import com.ll.biztrip.domain.trip.trip.dto.TripLegDto;
import com.ll.biztrip.domain.trip.trip.dto.TripPlanDto;
import com.ll.biztrip.domain.trip.trip.entity.TripLeg;
import com.ll.biztrip.domain.trip.trip.entity.TripPlan;
import com.ll.biztrip.domain.trip.trip.repository.TripLegRepository;
import com.ll.biztrip.domain.trip.trip.repository.TripPlanRepository;
import com.ll.biztrip.global.enums.Msg;
import com.ll.biztrip.global.enums.TransportType;
import com.ll.biztrip.global.exceptions.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TripService {

    private final TripPlanRepository tripPlanRepository;
    private final TripLegRepository tripLegRepository;

    @Transactional
    public void addTripPlan(TripPlanDto tripPlanDto, Member member){

        TripPlan tripPlan = TripPlan.builder()
                .startAddress(tripPlanDto.getStartAddress())
                .endAddress(tripPlanDto.getEndAddress())
                .planName(tripPlanDto.getPlanName())
                .member(member)
                .build();

        tripPlanRepository.save(tripPlan);
    }

    @Transactional
    public TripPlan addLeg(Long tripPlanId, TripLegDto tripLegDto) {

        TripPlan tripPlan = tripPlanRepository.findById(tripPlanId)
                .orElseThrow(()-> new GlobalException(
                        Msg.E404_0_DATA_NOT_FOUND.getCode(),
                        Msg.E404_0_DATA_NOT_FOUND.getMsg()));

        TripLeg tripLeg = TripLeg.builder()
                .tripPlan(tripPlan)
                .transportType(TransportType.valueOf(tripLegDto.getTransportType()))
                .transportId(tripLegDto.getTransportId())
                .arrivalName(tripLegDto.getArrivalName())
                .departureName(tripLegDto.getDepartureName())
                .departureTime(tripLegDto.getDepartureTime())
                .arrivalTime(tripLegDto.getArrivalTime())
                .build();

        tripPlan.getLegs().add(tripLeg);

        return tripPlanRepository.save(tripPlan);
    }
}
