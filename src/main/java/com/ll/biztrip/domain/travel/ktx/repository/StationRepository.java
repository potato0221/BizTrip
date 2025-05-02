package com.ll.biztrip.domain.travel.ktx.repository;

import com.ll.biztrip.domain.travel.ktx.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {

    boolean existsByStationId(String stationId);
}
