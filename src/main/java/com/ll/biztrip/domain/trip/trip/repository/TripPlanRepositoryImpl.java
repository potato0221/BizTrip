package com.ll.biztrip.domain.trip.trip.repository;

import com.ll.biztrip.domain.trip.trip.entity.QTripLeg;
import com.ll.biztrip.domain.trip.trip.entity.QTripPlan;
import com.ll.biztrip.domain.trip.trip.entity.TripPlan;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
public class TripPlanRepositoryImpl implements CustomTripPlanRepository{

    private final JPAQueryFactory queryFactory;

    QTripPlan tripPlan = QTripPlan.tripPlan;
    QTripLeg tripLeg = QTripLeg.tripLeg;

    @Override
    public List<TripPlan> findTodayPlans(){
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        return queryFactory
                .selectFrom(tripPlan)
                .distinct()
                .join(tripPlan.legs, tripLeg).fetchJoin()
                .where(tripLeg.departureTime.between(startOfDay, endOfDay))
                .fetch();
    }
}
