package com.projectX.ChargerReserv.domain.reservation.dto;

import lombok.Builder;

@Builder
public record CreateReservationCommand(
        Long userId,
        String vehicleNumber,
        Long chargerId
) {
}
