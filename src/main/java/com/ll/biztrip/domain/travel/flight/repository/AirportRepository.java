package com.ll.biztrip.domain.travel.flight.repository;

import com.ll.biztrip.domain.travel.flight.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepository extends JpaRepository<Airport, Long> {

    boolean existsByAirportId(String airportId);
}
