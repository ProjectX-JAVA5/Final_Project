package com.projectX.ChargerReserv.domain.reservation.service;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerEntity;
import com.projectX.ChargerReserv.domain.charger.entity.ChargerStatus;
import com.projectX.ChargerReserv.domain.charger.repository.ChargerRepository;
import com.projectX.ChargerReserv.domain.reservation.dto.ConfirmReservationCommand;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationEntity;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationStatus;
import com.projectX.ChargerReserv.domain.reservation.repository.ReservationRepository;
import com.projectX.ChargerReserv.global.error.NoExistException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class ReservationConfirmationService {

    private final ReservationRepository reservationRepository;
    private final ChargerRepository chargerRepository;

    public void confirmReservation(ConfirmReservationCommand command) {
        ChargerEntity charger = chargerRepository.findById(command.chargerId())
                .orElseThrow(() -> new NoExistException("충전기를 찾을 수 없습니다."));
        ReservationEntity reservation = reservationRepository.findByCharger_UniqueChargerIdAndVehicleNumber(
                command.chargerId(), command.vehicleNumber())
                .orElseThrow(() -> new NoExistException("예약을 찾을 수 없습니다."));
        validateReservation(charger, reservation);
        reservation.confirm();
        reservationRepository.save(reservation);
    }

    private void validateReservation(ChargerEntity charger, ReservationEntity reservation) {
        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("이미 확인된 예약입니다.");
        }

        if (charger.getStatus() != ChargerStatus.AVAILABLE) {
            throw new IllegalStateException("사용할 수 없는 충전기입니다.");
        }
    }
}
