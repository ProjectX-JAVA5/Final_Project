package com.projectX.ChargerReserv.domain.reservation.repository;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerEntity;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationEntity;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationStatus;
import com.projectX.ChargerReserv.domain.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    /**
     * 특정 충전기 ID와 차량 번호를 기준으로 예약 정보를 조회합니다.
     *
     * @param uniqueChargerId 충전기의 고유 ID
     * @param vehicleNumber 차량 번호
     * @return 찾아진 예약 정보를 포함하는 Optional 객체
     */
    Optional<ReservationEntity> findByCharger_UniqueChargerIdAndVehicleNumber(Long uniqueChargerId, String vehicleNumber);

    /**
     * 특정 사용자가 특정 상태의 예약을 가지고 있는지 여부를 반환합니다.
     *
     * @param user 사용자 엔티티
     * @param status 예약 상태
     * @return 상태가 존재하면 true, 그렇지 않으면 false
     */
    boolean existsByUserAndStatus(UserEntity user, ReservationStatus status);

    /**
     * 특정 충전기가 특정 상태의 예약을 가지고 있는지 여부를 반환합니다.
     *
     * @param charger 충전기 엔티티
     * @param status 예약 상태
     * @return 상태가 존재하면 true, 그렇지 않으면 false
     */
    boolean existsByChargerAndStatus(ChargerEntity charger, ReservationStatus status);

    /**
     * 사용자 ID와 생성된 날짜의 범위를 기준으로 예약 목록을 조회하며,
     * 결과는 예약 생성 날짜 기준으로 최신 순으로 정렬됩니다.
     *
     * @param userId 사용자 ID
     * @param startDate 검색 시작 날짜, null 가능
     * @param endDate 검색 종료 날짜, null 가능
     * @param pageable 페이지네이션 및 정렬 정보
     * @return 해당 기간과 사용자 ID에 맞는 예약 목록의 페이지
     */
    @Query("SELECT r FROM ReservationEntity r WHERE r.user.userId = :userId " +
            "AND FUNCTION('DATE', r.createdAt) >= :startDate " +
            "AND FUNCTION('DATE', r.createdAt) <= :endDate " +
            "ORDER BY r.createdAt DESC")
    Page<ReservationEntity> findAllByUserAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
}
