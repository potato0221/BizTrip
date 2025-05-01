package com.ll.biztrip.domain.travel.ktx.entity;

import com.ll.biztrip.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
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

    private String stationName;

    @ManyToOne(optional = false)
    private City cityCode;
}
