package com.projectX.ChargerReserv.domain.reservation.service;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerEntity;
import com.projectX.ChargerReserv.domain.charger.entity.ChargerStatus;
import com.projectX.ChargerReserv.domain.charger.repository.ChargerRepository;
import com.projectX.ChargerReserv.domain.reservation.dto.command.CreateReservationCommand;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationEntity;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationStatus;
import com.projectX.ChargerReserv.domain.reservation.repository.ReservationRepository;
import com.projectX.ChargerReserv.domain.user.entity.UserEntity;
import com.projectX.ChargerReserv.domain.user.repository.UserRepository;
import com.projectX.ChargerReserv.global.config.RabbitMQConfig;
import com.projectX.ChargerReserv.global.error.NoExistException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * 예약 생성 서비스 클래스.
 * 예약 생성과 관련된 비즈니스 로직을 처리합니다.
 */
@RequiredArgsConstructor
@Transactional
@Service
public class ReservationCreationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ChargerRepository chargerRepository;
    private final RabbitTemplate rabbitTemplate;

    /**
     * 새로운 예약을 생성합니다.
     *
     * @param command 예약 생성 명령 객체.
     * @return 생성된 예약의 ID.
     */
    public Long createReservation(CreateReservationCommand command) {
        UserEntity user = userRepository.findById(command.userId())
                .orElseThrow(() -> new NoExistException("사용자를 찾을 수 없습니다."));
        ChargerEntity charger = chargerRepository.findById(command.chargerId())
                .orElseThrow(() -> new NoExistException("충전기를 찾을 수 없습니다."));

        checkIfChargerAvailable(charger);
        checkUserActiveReservations(user);

        changeChargerStatusToReserved(charger);

        ReservationEntity reservation = createAndSaveReservation(command, user, charger);
        sendExpirationMessage(reservation.getId());

        return reservation.getId();
    }

    private void checkIfChargerAvailable(ChargerEntity charger) {
        if (charger.getStatus() != ChargerStatus.AVAILABLE) {
            throw new IllegalStateException("충전기가 예약 가능한 상태가 아닙니다.");
        }
    }

    private void checkUserActiveReservations(UserEntity user) {
        boolean hasActiveReservations = reservationRepository.existsByUserAndStatus(user, ReservationStatus.PENDING);
        if (hasActiveReservations) {
            throw new IllegalStateException("사용자가 이미 활성화된 예약을 가지고 있습니다.");
        }
    }

    private void changeChargerStatusToReserved(ChargerEntity charger) {
        charger.reserve();
        chargerRepository.save(charger);
    }

    private ReservationEntity createAndSaveReservation(CreateReservationCommand command, UserEntity user, ChargerEntity charger) {
        ReservationEntity reservation = ReservationFactory.create(command.vehicleNumber(), user, charger);
        return reservationRepository.save(reservation);
    }

    private void sendExpirationMessage(Long reservationId) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.RESERVATION_EXCHANGE,
                RabbitMQConfig.RESERVATION_ROUTING_KEY,
                reservationId
        );
    }
}
