package com.projectX.ChargerReserv.domain.reservation.event;

import com.projectX.ChargerReserv.domain.reservation.entity.ReservationStatus;

public record ReservationEvent(
        Long reservationId,
        Long chargerId,
        ReservationStatus status
) {
}
