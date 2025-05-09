package com.ll.biztrip.domain.weather.weather.repository;

import com.ll.biztrip.domain.weather.weather.entity.LocationCodeMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationCodeMappingRepository extends JpaRepository<LocationCodeMapping, Long>{
    LocationCodeMapping findByTripPlanId(Long tripPlanId);
}
