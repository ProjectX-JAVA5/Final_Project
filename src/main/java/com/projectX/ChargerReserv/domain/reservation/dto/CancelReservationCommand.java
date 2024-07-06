package com.projectX.ChargerReserv.domain.reservation.dto;

public record CancelReservationCommand(
        Long reservationId,
        Long userId
) {
}
