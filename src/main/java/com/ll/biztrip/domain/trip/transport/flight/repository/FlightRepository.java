package com.ll.biztrip.domain.trip.transport.flight.repository;

import com.ll.biztrip.domain.member.member.entity.Member;
import com.ll.biztrip.domain.trip.transport.flight.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    boolean existsByMemberAndDepartureNameAndArrivalNameAndDepartureTimeAndArrivalTimeAndFlightNumberAndAirline(
            Member member,
            String departureName,
            String arrivalName,
            LocalDateTime departureTime,
            LocalDateTime arrivalTime,
            String flightNumber,
            String airline
    );

    List<Flight> findFlightByMember(Member member);
}
