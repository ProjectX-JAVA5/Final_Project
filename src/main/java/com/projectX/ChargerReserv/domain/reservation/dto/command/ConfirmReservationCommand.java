package com.projectX.ChargerReserv.domain.reservation.dto.command;

import lombok.Builder;

@Builder
public record ConfirmReservationCommand(
        String vehicleNumber,
        Long chargerId
) {
}
