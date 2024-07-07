package com.projectX.ChargerReserv.domain.reservation.service;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerEntity;
import com.projectX.ChargerReserv.domain.chargingStation.entity.ChargingStationEntity;
import com.projectX.ChargerReserv.domain.reservation.dto.command.ReservationQueryCommand;
import com.projectX.ChargerReserv.domain.reservation.dto.response.ReservationListResponse;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationEntity;
import com.projectX.ChargerReserv.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationQueryService {

    private final ReservationRepository reservationRepository;

    /**
     * 사용자 ID와 날짜 범위를 기준으로 예약 목록을 조회합니다.
     *
     * @param command 사용자 ID와 날짜 범위가 포함된 명령 객체
     * @return 조회된 예약 목록
     */
    @Transactional(readOnly = true)
    public Page<ReservationListResponse> getReservations(ReservationQueryCommand command) {
        Page<ReservationEntity> reservations = reservationRepository.findAllByUserAndDateRange(
                command.userId(),
                command.startDate(),
                command.endDate(),
                command.pageable()
        );
        return reservations.map(reservation -> {
            ChargerEntity charger = reservation.getCharger();
            ChargingStationEntity station = charger.getStation();

            return new ReservationListResponse(
                    List.of(new ReservationListResponse.ReservationSummary(
                            reservation.getId(),
                            reservation.getReservationNumber(),
                            reservation.getVehicleNumber(),
                            reservation.getStartAt(),
                            reservation.getEndAt(),
                            reservation.getStatus(),
                            new ReservationListResponse.ChargerSummary(
                                    charger.getChargerId(),
                                    charger.getType(),
                                    charger.getStatus(),
                                    new ReservationListResponse.ChargingStationSummary(
                                            station.getChargingStationId(),
                                            station.getName(),
                                            station.getAddress()
                                    )
                            )
                    ))
            );
        });
    }
}
