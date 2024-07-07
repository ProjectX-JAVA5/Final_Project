package com.projectX.ChargerReserv.domain.reservation.service;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerEntity;
import com.projectX.ChargerReserv.domain.chargingStation.entity.ChargingStationEntity;
import com.projectX.ChargerReserv.domain.reservation.dto.command.ReservationDetailQueryCommand;
import com.projectX.ChargerReserv.domain.reservation.dto.command.ReservationQueryCommand;
import com.projectX.ChargerReserv.domain.reservation.dto.response.ReservationDetailResponse;
import com.projectX.ChargerReserv.domain.reservation.dto.response.ReservationListResponse;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationEntity;
import com.projectX.ChargerReserv.domain.reservation.repository.ReservationRepository;
import com.projectX.ChargerReserv.global.error.NoExistException;
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
        return reservations.map(this::mapToReservationListResponse);
    }

    /**
     * 예약 상세 정보를 조회합니다.
     *
     * @param command 예약 상세 조회에 필요한 명령 객체
     * @return 조회된 예약 상세 정보
     */
    @Transactional(readOnly = true)
    public ReservationDetailResponse getReservationDetail(ReservationDetailQueryCommand command) {
        ReservationEntity reservation = reservationRepository.findById(command.reservationId())
                .orElseThrow(() -> new NoExistException("예약을 찾을 수 없습니다. : " + command.reservationId()));

        if (!reservation.getUser().getUserId().equals(command.userId())) {
            throw new IllegalArgumentException("예약 상세 정보를 조회할 권한이 없습니다.");
        }

        return mapToReservationDetailResponse(reservation);
    }

    private ReservationListResponse mapToReservationListResponse(ReservationEntity reservation) {
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
                                charger.getUniqueChargerId(),
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
    }

    private ReservationDetailResponse mapToReservationDetailResponse(ReservationEntity reservation) {
        return new ReservationDetailResponse(
                reservation.getId(),
                reservation.getReservationNumber(),
                reservation.getVehicleNumber(),
                reservation.getStartAt(),
                reservation.getEndAt(),
                reservation.getStatus(),
                reservation.getChargerStartAt(),
                new ReservationDetailResponse.ChargerDetail(
                        reservation.getCharger().getUniqueChargerId(),
                        reservation.getCharger().getType(),
                        reservation.getCharger().getStatus(),
                        new ReservationDetailResponse.ChargingStationDetail(
                                reservation.getCharger().getStation().getChargingStationId(),
                                reservation.getCharger().getStation().getName(),
                                reservation.getCharger().getStation().getLatitude(),
                                reservation.getCharger().getStation().getLongitude(),
                                reservation.getCharger().getStation().getAddress(),
                                reservation.getCharger().getStation().getZcode(),
                                reservation.getCharger().getStation().getZscode(),
                                reservation.getCharger().getStation().getUseTime(),
                                reservation.getCharger().getStation().getBusiId()
                        )
                )
        );
    }
}
