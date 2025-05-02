package com.ll.biztrip.domain.travel.ktx.repository;

import com.ll.biztrip.domain.travel.ktx.entity.KtxCity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KtxCityRepository extends JpaRepository<KtxCity, Long> {
    boolean existsByCityCode(String cityCode);
}
