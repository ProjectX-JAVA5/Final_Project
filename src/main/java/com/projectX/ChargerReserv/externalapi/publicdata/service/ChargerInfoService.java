package com.projectX.ChargerReserv.externalapi.publicdata.service;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerEntity;
import com.projectX.ChargerReserv.externalapi.publicdata.dto.response.ChargerInfoResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.util.List;

public interface ChargerInfoService {

    public ChargerInfoResponse saveAllCharger(Long pageNo, Long numofRows, String zcode, String zscode) throws IOException;

    public ChargerInfoResponse saveAllChargerByStatId(Long pageNo, Long numofRows, String zcode, String zscode, String statId) throws IOException;

    public ChargerInfoResponse saveCharger(String statId, Long chgerId) throws IOException;
}
