package com.ll.biztrip.domain.trip.transport.train.repository;

import com.ll.biztrip.domain.trip.transport.train.entity.TrainCity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainCityRepository extends JpaRepository<TrainCity, Long> {
    boolean existsByCityCode(String cityCode);
}
