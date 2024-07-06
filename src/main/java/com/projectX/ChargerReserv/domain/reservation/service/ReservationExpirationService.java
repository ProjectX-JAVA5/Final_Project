package com.projectX.ChargerReserv.domain.reservation.service;

import com.projectX.ChargerReserv.domain.reservation.entity.ReservationStatus;
import com.projectX.ChargerReserv.domain.reservation.repository.ReservationRepository;
import com.projectX.ChargerReserv.global.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * 예약 만료 처리 서비스.
 * RabbitMQ에서 메시지를 수신하고 예약을 만료 상태로 변경합니다.
 */
@RequiredArgsConstructor
@Service
public class ReservationExpirationService {

    private final ReservationRepository reservationRepository;

    /**
     * 예약 만료 메시지를 처리합니다.
     *
     * @param reservationId 예약 ID.
     */
    @RabbitListener(queues = RabbitMQConfig.RESERVATION_DLQ)
    public void handleReservationExpiration(Long reservationId) {
        reservationRepository.findById(reservationId).ifPresent(reservation -> {
            if (reservation.getStatus() == ReservationStatus.PENDING && reservation.isExpired()) {
                reservation.markAsFailed();
                reservationRepository.save(reservation);
            }
        });
    }
}
