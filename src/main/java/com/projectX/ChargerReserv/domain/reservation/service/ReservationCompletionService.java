package com.projectX.ChargerReserv.domain.reservation.service;

import com.projectX.ChargerReserv.domain.reservation.entity.ReservationEntity;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationStatus;
import com.projectX.ChargerReserv.domain.reservation.event.ReservationEvent;
import com.projectX.ChargerReserv.domain.reservation.repository.ReservationRepository;
import com.projectX.ChargerReserv.global.error.IllegalArgumentException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class ReservationCompletionService {

    private final ReservationRepository reservationRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void completeReservation(Long reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new IllegalArgumentException("예약 상태가 완료할 수 있는 상태가 아닙니다.");
        }

        reservation.complete();
        reservationRepository.save(reservation);

        eventPublisher.publishEvent(new ReservationEvent(reservation.getId(), reservation.getCharger().getUniqueChargerId(), ReservationStatus.COMPLETED));
    }
}
