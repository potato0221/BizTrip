package com.ll.biztrip.domain.trip.transport.flight.entity;

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
public class Airline extends BaseEntity {

    @Column(unique = true, length = 20)
    private String airlineId;

    @Column(length = 20)
    private String airlineName;
}
