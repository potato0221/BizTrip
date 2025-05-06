package com.ll.biztrip.domain.travel.flight.repository;

import com.ll.biztrip.domain.travel.flight.entity.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirlineRepository extends JpaRepository<Airline, Long> {
}
