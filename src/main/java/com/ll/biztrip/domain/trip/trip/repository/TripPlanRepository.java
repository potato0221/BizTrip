package com.ll.biztrip.domain.trip.trip.repository;

import com.ll.biztrip.domain.trip.trip.entity.TripPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripPlanRepository extends JpaRepository<TripPlan, Long> {
}
