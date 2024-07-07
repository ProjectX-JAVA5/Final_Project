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
        ReservationEntity reservation = reservationRepository.findByCharger_UniqueChargerIdAndVehicleNumber(
                        command.chargerId(), command.vehicleNumber())
                .orElseThrow(() -> new NoExistException("예약을 찾을 수 없습니다."));

        validateReservation(charger, reservation);

        reservation.confirm();
        reservationRepository.save(reservation);

        eventPublisher.publishEvent(new ReservationEvent(reservation.getId(), charger.getUniqueChargerId(), ReservationStatus.CONFIRMED));
    }

    private void validateReservation(ChargerEntity charger, ReservationEntity reservation) {
        if (reservation.getStatus() != ReservationStatus.PENDING) {
            handleInvalidReservationStatus(reservation);
        }
        if (charger.getStatus() != ChargerStatus.RESERVED) {
            throw new IllegalStateException("충전기가 예약되지 않았습니다.");
        }
    }

    private void handleInvalidReservationStatus(ReservationEntity reservation) {
        switch (reservation.getStatus()) {
            case CONFIRMED:
                throw new IllegalStateException("이미 확인된 예약입니다.");
            case CANCELLED:
                throw new IllegalStateException("취소된 예약입니다.");
            case FAILED:
                throw new IllegalStateException("예약 시간이 초과되었습니다.");
            case COMPLETED:
                throw new IllegalStateException("이미 완료된 예약입니다.");
            default:
                throw new IllegalStateException("알 수 없는 예약 상태입니다.");
        }
    }
}
