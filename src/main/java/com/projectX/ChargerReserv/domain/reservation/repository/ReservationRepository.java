package com.projectX.ChargerReserv.domain.reservation.repository;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerEntity;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationEntity;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationStatus;
import com.projectX.ChargerReserv.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    Optional<ReservationEntity> findByCharger_UniqueChargerIdAndVehicleNumber(Long uniqueChargerId, String vehicleNumber);

    boolean existsByUserAndStatus(UserEntity user, ReservationStatus status);

    boolean existsByChargerAndStatus(ChargerEntity charger, ReservationStatus status);
}
