package com.projectX.ChargerReserv.domain.reservation.dto.command;

import lombok.Builder;

@Builder
public record ReservationDetailQueryCommand(
        Long reservationId,
        Long userId
) {
}
