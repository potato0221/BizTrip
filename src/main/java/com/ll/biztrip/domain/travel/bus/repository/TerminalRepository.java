package com.ll.biztrip.domain.travel.bus.repository;

import com.ll.biztrip.domain.travel.bus.entity.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TerminalRepository extends JpaRepository<Terminal, Long> {
    boolean existsByTerminalId(String terminalId);
}
