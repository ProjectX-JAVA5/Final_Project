package com.projectX.ChargerReserv.domain.chargingStation.repository;

import com.projectX.ChargerReserv.domain.chargingStation.entity.ChargingStationEntity;
import com.projectX.ChargerReserv.domain.chargingStation.entity.RegionDetail;
import com.projectX.ChargerReserv.domain.chargingStation.entity.StatRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<ChargingStationEntity, String> {
    List<ChargingStationEntity> findAllByZcode(StatRegion zcode);

    List<ChargingStationEntity> findAllByZscode(RegionDetail zscode);

    List<ChargingStationEntity> findAllByZcodeAndZscode(StatRegion zcode, RegionDetail zscode);
}
