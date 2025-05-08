package com.ll.biztrip.domain.trip.trip.repository;

import com.ll.biztrip.domain.trip.trip.entity.TripLeg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripLegRepository extends JpaRepository<TripLeg, Long> {
}
