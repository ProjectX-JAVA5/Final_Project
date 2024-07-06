package com.projectX.ChargerReserv.domain.chargingStation.service;

import com.projectX.ChargerReserv.domain.chargingStation.entity.ChargingStationEntity;

import java.util.List;

public interface StationService {
    public void saveStation(ChargingStationEntity chargingStation);
    public List<ChargingStationEntity> getAllStation(String zcode, String zscode);
}
