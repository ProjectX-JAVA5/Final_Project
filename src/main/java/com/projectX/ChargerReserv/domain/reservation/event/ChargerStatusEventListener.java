package com.projectX.ChargerReserv.domain.reservation.event;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerEntity;
import com.projectX.ChargerReserv.domain.charger.repository.ChargerRepository;
import com.projectX.ChargerReserv.global.error.NoExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class ChargerStatusEventListener {

    private final ChargerRepository chargerRepository;

    @EventListener
    @Transactional
    public void handleReservationEvent(ReservationEvent event) {
        ChargerEntity charger = chargerRepository.findById(event.chargerId())
                .orElseThrow(() -> new NoExistException("충전기를 찾을 수 없습니다."));

        switch (event.status()) {
            case PENDING -> charger.reserved();
            case CONFIRMED -> charger.charging();
            case CANCELLED, FAILED, COMPLETED -> charger.available();
            default -> throw new IllegalArgumentException("올바르지 않은 예약 상태입니다.");
        }

        chargerRepository.save(charger);
    }
}
