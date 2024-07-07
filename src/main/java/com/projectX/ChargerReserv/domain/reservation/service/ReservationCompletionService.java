package com.projectX.ChargerReserv.domain.reservation.service;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerEntity;
import com.projectX.ChargerReserv.domain.charger.repository.ChargerRepository;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationEntity;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationStatus;
import com.projectX.ChargerReserv.domain.reservation.repository.ReservationRepository;
import com.projectX.ChargerReserv.global.error.IllegalArgumentException;
import com.projectX.ChargerReserv.global.error.NoExistException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class ReservationCompletionService {

    private final ReservationRepository reservationRepository;
    private final ChargerRepository chargerRepository;

    public void completeReservation(Long reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new IllegalArgumentException("예약 상태가 완료할 수 있는 상태가 아닙니다.");
        }

        ChargerEntity charger = chargerRepository.findById(reservation.getCharger().getUniqueChargerId())
                .orElseThrow(() -> new NoExistException("충전기를 찾을 수 없습니다."));

        charger.complete();
        chargerRepository.save(charger);

        reservation.complete();
        reservationRepository.save(reservation);
    }
}
