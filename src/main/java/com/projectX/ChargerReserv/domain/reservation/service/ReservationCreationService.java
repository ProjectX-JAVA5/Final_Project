package com.projectX.ChargerReserv.domain.reservation.service;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerEntity;
import com.projectX.ChargerReserv.domain.charger.repository.ChargerRepository;
import com.projectX.ChargerReserv.domain.reservation.dto.CreateReservationCommand;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationEntity;
import com.projectX.ChargerReserv.domain.reservation.repository.ReservationRepository;
import com.projectX.ChargerReserv.domain.user.entity.UserEntity;
import com.projectX.ChargerReserv.domain.user.repository.UserRepository;
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
        ReservationEntity reservation = ReservationFactory.create(command.vehicleNumber(), user, charger);
        return reservationRepository.save(reservation).getId();
    }
}
