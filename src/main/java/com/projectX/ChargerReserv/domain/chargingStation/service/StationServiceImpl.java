package com.projectX.ChargerReserv.domain.chargingStation.service;

import com.projectX.ChargerReserv.domain.chargingStation.entity.ChargingStationEntity;
import com.projectX.ChargerReserv.domain.chargingStation.entity.RegionDetail;
import com.projectX.ChargerReserv.domain.chargingStation.entity.StatRegion;
import com.projectX.ChargerReserv.domain.chargingStation.repository.StationRepository;
import com.projectX.ChargerReserv.global.error.NoExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;

    @Override
    public void saveStation(ChargingStationEntity chargingStation) {
        stationRepository.save(chargingStation);
    }

    @Override
    public List<ChargingStationEntity> getAllStation(String zcode, String zscode) {

        List<ChargingStationEntity> stationEntityList;
        StatRegion statRegion = null;
        RegionDetail regionDetail = null;

        if(zcode != null){
            statRegion = StatRegion.fromCode(zcode);
        }

        if(zscode != null){
            regionDetail = RegionDetail.fromCode(zscode);
        }

        if (zcode != null && !zcode.isEmpty() && zscode != null && !zscode.isEmpty()) {
            stationEntityList = stationRepository.findAllByZcodeAndZscode(statRegion, regionDetail);
        } else if (zcode != null && !zcode.isEmpty()) {
            stationEntityList = stationRepository.findAllByZcode(statRegion);
        } else if (zscode != null && !zscode.isEmpty()) {
            stationEntityList = stationRepository.findAllByZscode(regionDetail);
        } else {
            stationEntityList = stationRepository.findAll();
        }
        if(stationEntityList.isEmpty()){
            throw new NoExistException("충전소 정보를 불러올 수 없습니다.");
        }

        return stationEntityList;
    }

}
