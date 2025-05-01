package com.ll.biztrip.domain.travel.ktx.entity;

import com.ll.biztrip.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
@Setter
@Getter
@ToString(callSuper = true)
public class City extends BaseEntity {

    private String cityCode;

    private String cityName;
}
