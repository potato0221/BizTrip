package com.ll.biztrip.domain.trip.trip.repository;

import com.ll.biztrip.domain.trip.trip.entity.TripPlan;

import java.util.List;

public interface CustomTripPlanRepository {

    List<TripPlan> findTodayPlans();
}
