package com.ll.biztrip.domain.travel.train.repository;

import com.ll.biztrip.domain.travel.train.entity.TrainCity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainCityRepository extends JpaRepository<TrainCity, Long> {
    boolean existsByCityCode(String cityCode);
}
