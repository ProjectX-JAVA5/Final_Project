package com.projectX.ChargerReserv.externalapi.publicdata.service;

import com.projectX.ChargerReserv.externalapi.publicdata.dto.response.StationInfoResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public interface StationInfoService {
    public StationInfoResponse saveAllStationInfoByzcode(String zcode, Long rows, Long pageNo) throws IOException;
}
