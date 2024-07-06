package com.projectX.ChargerReserv.domain.chargingStation.controller;

import com.projectX.ChargerReserv.domain.chargingStation.dto.StationResponse;
import com.projectX.ChargerReserv.domain.chargingStation.entity.ChargingStationEntity;
import com.projectX.ChargerReserv.domain.chargingStation.service.StationService;
import com.projectX.ChargerReserv.externalapi.publicdata.dto.response.StationInfoResponse;
import com.projectX.ChargerReserv.externalapi.publicdata.service.StationInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/stations")
@RequiredArgsConstructor
public class StationController {

    private final StationInfoService stationInfoService;
    private final StationService stationService;

    @PostMapping("")
    public ResponseEntity<StationInfoResponse> saveAllSation(
            @RequestHeader(value = "zcode", defaultValue = "27") String zcode,
            @RequestHeader(value = "numOfRows") Long rows,
            @RequestHeader(value = "pageNo") Long pageNo
    ) throws IOException {
        StationInfoResponse stationInfoResponse = stationInfoService.saveAllStationInfoByzcode(zcode, rows, pageNo);

        return ResponseEntity.ok().body(stationInfoResponse);
    }

    @GetMapping("")
    public ResponseEntity<List<StationResponse>> getAllStation(
            @RequestHeader(value = "zcode", required = false) String zcode,
            @RequestHeader(value = "zscode", required = false) String zscode
    ){
        List<ChargingStationEntity> stationEntityList = stationService.getAllStation(zcode, zscode);
        List<StationResponse> stationResponseList = new ArrayList<>();

        for(ChargingStationEntity stationEntity : stationEntityList){
            StationResponse stationResponse = StationResponse.builder()
                    .name(stationEntity.getName())
                    .latitude(stationEntity.getLatitude())
                    .longitude(stationEntity.getLongitude())
                    .address(stationEntity.getAddress())
                    .useTime(stationEntity.getUseTime())
                    .busiId(stationEntity.getBusiId())
                    .build();
            stationResponseList.add(stationResponse);
        }
        return ResponseEntity.ok().body(stationResponseList);
    }
}
