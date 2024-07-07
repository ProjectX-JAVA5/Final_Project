package com.projectX.ChargerReserv.domain.reservation.dto.command;

import lombok.Builder;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@Builder
public record ReservationQueryCommand(
        Long userId,
        LocalDate startDate,
        LocalDate endDate,
        Pageable pageable
) {
}
