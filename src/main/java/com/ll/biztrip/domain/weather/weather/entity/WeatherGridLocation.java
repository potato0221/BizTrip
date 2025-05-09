package com.ll.biztrip.domain.weather.weather.entity;

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
public class WeatherGridLocation extends BaseEntity {

    @Column(unique = true, length = 20)
    private String locationCode;

    @Column(length = 20)
    private String siDo;

    @Column(length = 20)
    private String siGunGu;

    @Column(length = 20)
    private String dong;

    private int gridX;

    private int gridY;
}
