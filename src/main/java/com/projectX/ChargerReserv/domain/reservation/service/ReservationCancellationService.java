package com.projectX.ChargerReserv.domain.reservation.service;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerEntity;
import com.projectX.ChargerReserv.domain.charger.repository.ChargerRepository;
import com.projectX.ChargerReserv.domain.reservation.dto.command.CancelReservationCommand;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationStatus;
import com.projectX.ChargerReserv.domain.reservation.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class ReservationCancellationService {

    private final ReservationRepository reservationRepository;
    private final ChargerRepository chargerRepository;

    public void cancelReservation(CancelReservationCommand command) {
        reservationRepository.findById(command.reservationId())
                .ifPresentOrElse(reservation -> {
                    if (!reservation.getUser().getUserId().equals(command.userId())) {
                        throw new IllegalArgumentException("예약을 취소할 권한이 없습니다.");
                    }
                    if (reservation.getStatus() != ReservationStatus.PENDING) {
                        throw new IllegalStateException("예약을 취소할 수 없는 상태입니다.");
                    }
                    ChargerEntity charger = chargerRepository.findById(reservation.getCharger().getChargerId())
                            .orElseThrow(() -> new IllegalArgumentException("충전기를 찾을 수 없습니다."));

                    charger.cancel();
                    chargerRepository.save(charger);

                    reservation.cancel();
                    reservationRepository.save(reservation);
                }, () -> {
                    throw new IllegalArgumentException("예약을 찾을 수 없습니다.");
                });
    }
}
