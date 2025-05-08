package com.ll.biztrip.domain.trip.trip.entity;

import com.ll.biztrip.global.enums.TransportType;
import com.ll.biztrip.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
@Setter
@Getter
@ToString(callSuper = true)
public class TripLeg extends BaseEntity {

    @ManyToOne
    private TripPlan tripPlan;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private TransportType transportType;

    private Long transportId;

    @Column(length = 50)
    private String departureName;

    @Column(length = 50)
    private String arrivalName;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;


}
