package com.ll.biztrip.domain.transport.train.repository;

import com.ll.biztrip.domain.transport.train.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {

    boolean existsByStationId(String stationId);
}
