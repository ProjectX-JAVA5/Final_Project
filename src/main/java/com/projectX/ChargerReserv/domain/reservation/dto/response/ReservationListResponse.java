package com.projectX.ChargerReserv.domain.reservation.dto.response;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerStatus;
import com.projectX.ChargerReserv.domain.charger.entity.ChargerType;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ReservationListResponse(
        List<ReservationSummary> reservations
) {

    public record ReservationSummary(
            Long id,
            String reservationNumber,
            String vehicleNumber,
            LocalDateTime startAt,
            LocalDateTime endAt,
            ReservationStatus status,
            ChargerSummary charger
    ) {
    }

    public record ChargerSummary(
            Long id,
            ChargerType type,
            ChargerStatus status,
            ChargingStationSummary station
    ) {
    }

    public record ChargingStationSummary(
            String id,
            String name,
            String address
    ) {
    }
}