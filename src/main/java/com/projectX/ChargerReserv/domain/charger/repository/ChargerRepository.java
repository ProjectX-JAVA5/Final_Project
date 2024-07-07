package com.projectX.ChargerReserv.domain.charger.repository;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerEntity;
import com.projectX.ChargerReserv.domain.chargingStation.entity.ChargingStationEntity;
import com.projectX.ChargerReserv.domain.chargingStation.entity.RegionDetail;
import com.projectX.ChargerReserv.domain.chargingStation.entity.StatRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChargerRepository extends JpaRepository<ChargerEntity, Long> {
    List<ChargerEntity> findAllByZscode(RegionDetail regionDetail);

    List<ChargerEntity> findAllByZcode(StatRegion statRegion);

    List<ChargerEntity> findAllByZcodeAndZscode(StatRegion statRegion, RegionDetail regionDetail);

    List<ChargerEntity> findAllByStation(ChargingStationEntity stationEntity);

    ChargerEntity findByStationAndChargerId(ChargingStationEntity stationEntity, Long chgerId);
}
