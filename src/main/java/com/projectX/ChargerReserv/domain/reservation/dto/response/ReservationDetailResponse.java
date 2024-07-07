package com.projectX.ChargerReserv.domain.reservation.dto.response;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerStatus;
import com.projectX.ChargerReserv.domain.charger.entity.ChargerType;
import com.projectX.ChargerReserv.domain.chargingStation.entity.Business;
import com.projectX.ChargerReserv.domain.chargingStation.entity.RegionDetail;
import com.projectX.ChargerReserv.domain.chargingStation.entity.StatRegion;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationDetailResponse(
       Long id,
       String reservationNumber,
       String vehicleNumber,
       LocalDateTime startAt,
       LocalDateTime endAt,
       ReservationStatus status,
       LocalDateTime chargerStartAt,
       ChargerDetail charger
) {

    public record ChargerDetail(
            Long id,
            ChargerType type,
            ChargerStatus status,
            ChargingStationDetail station
    ) {
    }

    public record ChargingStationDetail(
            Long id,
            String name,
            Double latitude,
            Double longitude,
            String address,
            StatRegion zcode,
            RegionDetail zscode,
            String useTime,
            Business busiId
    ) {
    }
}
