package com.projectX.ChargerReserv.domain.charger.service;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerEntity;

import java.util.List;

public interface ChargerService {
    public void saveCharger(ChargerEntity chargerEntity);

    public List<ChargerEntity> getAllCharger(String zcode, String zscode);

    public List<ChargerEntity> getAllChargerByStatId(String statId);

    public ChargerEntity getCharger(String statId, Long chgerId);

}
