package com.projectX.ChargerReserv.domain.reservation.dto.command;

import lombok.Builder;

@Builder
public record CreateReservationCommand(
        Long userId,
        String vehicleNumber,
        Long chargerId
) {
}
