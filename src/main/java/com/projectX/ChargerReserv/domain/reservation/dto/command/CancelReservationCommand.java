package com.projectX.ChargerReserv.domain.reservation.dto.command;

public record CancelReservationCommand(
        Long reservationId,
        Long userId
) {
}
