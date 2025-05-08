package com.ll.biztrip.domain.trip.transport.bus.repository;

import com.ll.biztrip.domain.member.member.entity.Member;
import com.ll.biztrip.domain.trip.transport.bus.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface BusRepository extends JpaRepository<Bus, Long> {

    boolean existsByMemberAndDepartureNameAndArrivalNameAndDepartureTimeAndArrivalTime(
            Member member,
            String departureName,
            String arrivalName,
            LocalDateTime departureTime,
            LocalDateTime arrivalTime
    );
}
