package com.ll.biztrip.domain.trip.transport.flight.repository;

import com.ll.biztrip.domain.trip.transport.flight.entity.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirlineRepository extends JpaRepository<Airline, Long> {
}
