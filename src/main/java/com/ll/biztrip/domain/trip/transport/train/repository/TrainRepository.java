package com.ll.biztrip.domain.trip.transport.train.repository;

import com.ll.biztrip.domain.member.member.entity.Member;
import com.ll.biztrip.domain.trip.transport.train.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TrainRepository extends JpaRepository<Train, Long> {

    boolean existsByMemberAndDepartureNameAndArrivalNameAndDepartureTimeAndArrivalTime(
            Member member,
            String departureName,
            String arrivalName,
            LocalDateTime departureTime,
            LocalDateTime arrivalTime
    );

    List<Train> findTrainByMember(Member member);
}
