package com.ll.biztrip.domain.travel.train.repository;

import com.ll.biztrip.domain.travel.train.entity.TrainType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainTypeRepository extends JpaRepository<TrainType, Long> {

    boolean existsByTrainId(String stationId);
}
