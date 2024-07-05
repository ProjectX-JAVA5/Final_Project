package com.projectX.ChargerReserv.domain.reservation.service;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerEntity;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationEntity;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationStatus;
import com.projectX.ChargerReserv.domain.user.entity.UserEntity;
import com.projectX.ChargerReserv.global.util.ReservationNumberGenerator;

import java.time.LocalDateTime;

public class ReservationFactory {

    public static ReservationEntity create(String vehicleNumber, UserEntity user, ChargerEntity charger) {
        String reservationNumber = ReservationNumberGenerator.generate();
        LocalDateTime startAt = LocalDateTime.now();
        LocalDateTime endAt = startAt.plusMinutes(30);

        return ReservationEntity.builder()
                .reservationNumber(reservationNumber)
                .vehicleNumber(vehicleNumber)
                .startAt(startAt)
                .endAt(endAt)
                .status(ReservationStatus.PENDING)
                .user(user)
                .charger(charger)
                .build();
    }
}
