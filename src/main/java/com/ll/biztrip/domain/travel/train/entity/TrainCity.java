package com.ll.biztrip.domain.travel.train.entity;

import com.ll.biztrip.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
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
public class TrainCity extends BaseEntity {

    @Column(unique = true, length = 10)
    private String cityCode;

    @Column(unique = true, length = 20)
    private String cityName;
}
