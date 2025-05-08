package com.ll.biztrip.domain.transport.train.entity;

import com.ll.biztrip.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
@Setter
@Getter
@ToString(callSuper = true)
public class Station extends BaseEntity {

    @Column(unique = true, length = 20)
    private String stationName;

    @Column(unique = true, length = 20)
    private String stationId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cityName", referencedColumnName = "cityName")
    private TrainCity trainCity;
}
