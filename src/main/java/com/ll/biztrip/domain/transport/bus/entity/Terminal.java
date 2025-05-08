package com.ll.biztrip.domain.transport.bus.entity;

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
public class Terminal extends BaseEntity {

    @Column(unique = true, length = 20)
    private String terminalId;

    @Column(length = 30)
    private String terminalName;
}
