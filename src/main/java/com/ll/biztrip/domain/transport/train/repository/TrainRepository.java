package com.ll.biztrip.domain.transport.train.repository;

import com.ll.biztrip.domain.member.member.entity.Member;
import com.ll.biztrip.domain.transport.train.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface TrainRepository extends JpaRepository<Train, Long> {

    boolean existsByMemberAndDepartureNameAndArrivalNameAndDepartureTimeAndArrivalTime(
            Member member,
            String departureName,
            String arrivalName,
            LocalDateTime departureTime,
            LocalDateTime arrivalTime
    );
}
