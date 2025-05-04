package com.ll.biztrip.domain.travel.bus.entity;

import com.ll.biztrip.domain.member.member.entity.Member;
import com.ll.biztrip.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
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
public class Bus extends BaseEntity {

    @Column(length = 50)
    private String departureName;

    @Column(length = 50)
    private String arrivalName;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;

    @Column(length = 20)
    private String busGrade;

    @ManyToOne
    private Member member;
}
