package com.projectX.ChargerReserv.domain.reservation.service;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerEntity;
import com.projectX.ChargerReserv.domain.charger.entity.ChargerStatus;
import com.projectX.ChargerReserv.domain.charger.repository.ChargerRepository;
import com.projectX.ChargerReserv.domain.reservation.dto.command.ConfirmReservationCommand;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationEntity;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationStatus;
import com.projectX.ChargerReserv.domain.reservation.event.ReservationEvent;
import com.projectX.ChargerReserv.domain.reservation.repository.ReservationRepository;
import com.projectX.ChargerReserv.global.error.NoExistException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class ReservationConfirmationService {

    private final ReservationRepository reservationRepository;
    private final ChargerRepository chargerRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void confirmReservation(ConfirmReservationCommand command) {
        ChargerEntity charger = chargerRepository.findById(command.chargerId())
                .orElseThrow(() -> new NoExistException("충전기를 찾을 수 없습니다."));
        ReservationEntity reservation = reservationRepository.findByCharger_UniqueChargerIdAndVehicleNumberAndStatus(
                        command.chargerId(), command.vehicleNumber(), ReservationStatus.PENDING)
                .orElseThrow(() -> new NoExistException("예약을 찾을 수 없습니다."));

        validateReservation(charger, reservation);

        reservation.confirm();
        reservationRepository.save(reservation);

        eventPublisher.publishEvent(new ReservationEvent(reservation.getId(), charger.getUniqueChargerId(), ReservationStatus.CONFIRMED));
    }

    private void validateReservation(ChargerEntity charger, ReservationEntity reservation) {
        if (charger.getStatus() != ChargerStatus.RESERVED) {
            throw new IllegalStateException("충전기가 예약되지 않았습니다.");
        }
    }
}
