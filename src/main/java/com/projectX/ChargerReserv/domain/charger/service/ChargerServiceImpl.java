package com.projectX.ChargerReserv.domain.charger.service;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerEntity;
import com.projectX.ChargerReserv.domain.charger.repository.ChargerRepository;
import com.projectX.ChargerReserv.domain.chargingStation.entity.ChargingStationEntity;
import com.projectX.ChargerReserv.domain.chargingStation.entity.RegionDetail;
import com.projectX.ChargerReserv.domain.chargingStation.entity.StatRegion;
import com.projectX.ChargerReserv.domain.chargingStation.repository.StationRepository;
import com.projectX.ChargerReserv.global.error.NoExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChargerServiceImpl implements ChargerService{

    private final ChargerRepository chargerRepository;
    private final StationRepository stationRepository;

    @Override
    public void saveCharger(ChargerEntity chargerEntity) {
        Optional <ChargerEntity> charger = Optional.ofNullable(chargerRepository.findByStationAndChargerId(chargerEntity.getStation(), chargerEntity.getChargerId()));
        if(charger.isEmpty()) {
            chargerRepository.save(chargerEntity);
        }
    }

    @Override
    public List<ChargerEntity> getAllCharger(String zcode, String zscode) {
        StatRegion statRegion = null;
        RegionDetail regionDetail = null;

        List<ChargerEntity> chargerEntityList;
        if(zcode != null){
            statRegion = StatRegion.fromCode(zcode);
        }

        if(zscode != null){
            regionDetail = RegionDetail.fromCode(zscode);
        }

        if (zcode != null && !zcode.isEmpty() && zscode != null && !zscode.isEmpty()) {
            chargerEntityList = chargerRepository.findAllByZcodeAndZscode(statRegion, regionDetail);
        } else if (zcode != null && !zcode.isEmpty()) {
            chargerEntityList = chargerRepository.findAllByZcode(statRegion);
        } else if (zscode != null && !zscode.isEmpty()) {
            chargerEntityList = chargerRepository.findAllByZscode(regionDetail);
        } else {
            chargerEntityList = chargerRepository.findAll();
        }
        if(chargerEntityList.isEmpty()){
            throw new NoExistException("충전소 정보를 불러올 수 없습니다.");
        }


        return chargerEntityList;
    }

    @Override
    public List<ChargerEntity> getAllChargerByStatId(String statId) {

        Optional<ChargingStationEntity> stationEntity = stationRepository.findById(statId);
        if(stationEntity.isEmpty()){
            throw new NoExistException("충전기 정보가 없습니다.");
        }

        List<ChargerEntity> chargerEntityList = chargerRepository.findAllByStation(stationEntity.get());

        if(chargerEntityList.isEmpty()){
            throw new NoExistException("충전소 정보를 불러올 수 없습니다.");
        }

        return chargerEntityList;
    }

    @Override
    public ChargerEntity getCharger(String statId, Long chgerId) {

        Optional<ChargingStationEntity> chargingStationEntity = stationRepository.findById(statId);
        if(chargingStationEntity.isEmpty()){
            throw new NoExistException("충전소 정보를 불러올 수 없습니다.");
        }

        return chargerRepository.findByStationAndChargerId(chargingStationEntity.get(),chgerId);
    }
}
