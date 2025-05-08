package com.ll.biztrip.domain.trip.transport.bus.repository;

import com.ll.biztrip.domain.trip.transport.bus.entity.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TerminalRepository extends JpaRepository<Terminal, Long> {
    boolean existsByTerminalId(String terminalId);
}
