package com.ll.biztrip.domain.transport.train.repository;

import com.ll.biztrip.domain.transport.train.entity.TrainType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainTypeRepository extends JpaRepository<TrainType, Long> {

    boolean existsByTrainId(String stationId);
}
