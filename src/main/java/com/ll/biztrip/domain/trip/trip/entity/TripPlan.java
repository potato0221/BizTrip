package com.ll.biztrip.domain.trip.trip.entity;

import com.ll.biztrip.domain.member.member.entity.Member;
import com.ll.biztrip.domain.weather.weather.entity.LocationCodeMapping;
import com.ll.biztrip.global.jpa.entity.BaseEntity;
import com.ll.biztrip.global.security.util.AESAttributeConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
@Setter
@Getter
@ToString(callSuper = true)
public class TripPlan extends BaseEntity {

    @Convert(converter = AESAttributeConverter.class)
    @Column(length = 254)
    private String startAddress;

    @Convert(converter = AESAttributeConverter.class)
    @Column(length = 254)
    private String endAddress;

    @Column(length = 100)
    private String planName;

    @ManyToOne
    private Member member;

    @OneToMany(mappedBy = "tripPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripLeg> legs = new ArrayList<>();

    @OneToOne(mappedBy = "tripPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private LocationCodeMapping locationCodeMapping;

    public void connectLocationMapping(LocationCodeMapping mapping) {
        this.locationCodeMapping = mapping;
        mapping.setTripPlan(this);
    }
}
