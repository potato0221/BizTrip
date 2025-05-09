package com.ll.biztrip.domain.weather.weather.entity;

import com.ll.biztrip.domain.trip.trip.entity.TripPlan;
import com.ll.biztrip.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;


@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
@Setter
@Getter
@ToString(callSuper = true)
public class LocationCodeMapping extends BaseEntity {

    @Column(unique = true, length = 20)
    private String locationCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, unique = true)
    private TripPlan tripPlan;
}
