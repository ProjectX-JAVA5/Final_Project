package com.projectX.ChargerReserv.domain.reservation.service;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerEntity;
import com.projectX.ChargerReserv.domain.charger.entity.ChargerStatus;
import com.projectX.ChargerReserv.domain.charger.repository.ChargerRepository;
import com.projectX.ChargerReserv.domain.reservation.dto.CreateReservationCommand;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationEntity;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationStatus;
import com.projectX.ChargerReserv.domain.reservation.repository.ReservationRepository;
import com.projectX.ChargerReserv.domain.user.entity.UserEntity;
import com.projectX.ChargerReserv.domain.user.repository.UserRepository;
import com.projectX.ChargerReserv.global.error.IllegalArgumentException;
import com.projectX.ChargerReserv.global.error.NoExistException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class ReservationCreationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ChargerRepository chargerRepository;

    public Long createReservation(CreateReservationCommand command) {
        UserEntity user = userRepository.findById(command.userId())
                .orElseThrow(() -> new NoExistException("사용자를 찾을 수 없습니다."));
        ChargerEntity charger = chargerRepository.findById(command.chargerId())
                .orElseThrow(() -> new NoExistException("충전기를 찾을 수 없습니다."));
        validateReservation(user, charger);
        ReservationEntity reservation = ReservationFactory.create(command.vehicleNumber(), user, charger);
        return reservationRepository.save(reservation).getId();
    }

    private void validateReservation(UserEntity user, ChargerEntity charger) {
        if (charger.getStatus() != ChargerStatus.AVAILABLE) {
            throw new IllegalArgumentException("사용할 수 없는 충전기입니다.");
        }

        boolean chargerHasPendingReservations = reservationRepository.existsByChargerAndStatus(charger, ReservationStatus.PENDING);
        if (chargerHasPendingReservations) {
            throw new IllegalStateException("해당 충전기에 대기 중인 예약이 있습니다.");
        }

        boolean userHasActiveReservations = reservationRepository.existsByUserAndStatus(user, ReservationStatus.PENDING);
        if (userHasActiveReservations) {
            throw new IllegalArgumentException("사용자가 이미 활성화된 예약을 가지고 있습니다.");
        }
    }
}
