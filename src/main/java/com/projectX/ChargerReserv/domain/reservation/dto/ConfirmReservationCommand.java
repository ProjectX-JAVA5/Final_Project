package com.projectX.ChargerReserv.domain.reservation.dto;

import lombok.Builder;

@Builder
public record ConfirmReservationCommand(
        String vehicleNumber,
        Long chargerId
) {
}
