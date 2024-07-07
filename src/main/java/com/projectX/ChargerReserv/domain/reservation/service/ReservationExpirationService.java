package com.projectX.ChargerReserv.domain.reservation.service;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerEntity;
import com.projectX.ChargerReserv.domain.charger.repository.ChargerRepository;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationStatus;
import com.projectX.ChargerReserv.domain.reservation.repository.ReservationRepository;
import com.projectX.ChargerReserv.global.config.RabbitMQConfig;
import com.projectX.ChargerReserv.global.error.NoExistException;
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
    private final ChargerRepository chargerRepository;

    /**
     * 예약 만료 메시지를 처리합니다.
     *
     * @param reservationId 예약 ID.
     */
    @RabbitListener(queues = RabbitMQConfig.RESERVATION_DLQ)
    public void handleReservationExpiration(Long reservationId) {
        reservationRepository.findById(reservationId).ifPresent(reservation -> {
            if (reservation.getStatus() == ReservationStatus.PENDING && reservation.isExpired()) {
                ChargerEntity charger = chargerRepository.findById(reservation.getCharger().getUniqueChargerId())
                        .orElseThrow(() -> new NoExistException("충전기를 찾을 수 없습니다."));
                charger.cancel();
                chargerRepository.save(charger);

                reservation.failed();
                reservationRepository.save(reservation);
            }
        });
    }
}
