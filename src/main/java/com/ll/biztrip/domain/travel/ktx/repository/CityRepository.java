package com.ll.biztrip.domain.travel.ktx.repository;

import com.ll.biztrip.domain.travel.ktx.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
    boolean existsByCityCode(String cityCode);
}
