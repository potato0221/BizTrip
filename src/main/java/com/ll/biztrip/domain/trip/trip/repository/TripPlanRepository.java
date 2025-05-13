package com.ll.biztrip.domain.trip.trip.repository;

import com.ll.biztrip.domain.member.member.entity.Member;
import com.ll.biztrip.domain.trip.trip.entity.TripPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripPlanRepository extends JpaRepository<TripPlan, Long>, CustomTripPlanRepository {

    List<TripPlan> findTripPlanByMember(Member member);

    void deleteByIdAndMember(Long id, Member member);
}
