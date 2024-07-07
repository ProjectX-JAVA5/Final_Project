package com.projectX.ChargerReserv.domain.charger.controller;

import com.projectX.ChargerReserv.domain.charger.dto.response.ChargerResponse;
import com.projectX.ChargerReserv.domain.charger.entity.ChargerEntity;
import com.projectX.ChargerReserv.domain.charger.service.ChargerService;
import com.projectX.ChargerReserv.externalapi.publicdata.dto.response.ChargerInfoResponse;
import com.projectX.ChargerReserv.externalapi.publicdata.service.ChargerInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/charger")
public class ChargerController {

    private final ChargerService chargerService;
    private final ChargerInfoService chargerInfoService;

    @PostMapping()
    public ResponseEntity<List<ChargerResponse>> saveAllCharger(
            @RequestHeader(value = "pageNo", defaultValue = "1") Long pageNo,
            @RequestHeader(value = "numofRows", defaultValue = "10") Long numofRows,
            @RequestHeader(value = "zcode", defaultValue = "27") String zcode,
            @RequestHeader(value = "zscode", defaultValue = "27260") String zscode // default: 대구스마트시티센터
    ) throws IOException {
        List<ChargerResponse> chargerList = chargerInfoService.saveAllCharger(pageNo, numofRows, zcode, zscode).getChargerList();

        return ResponseEntity.ok().body(chargerList);
    }

    @PostMapping("/{statId}")
    public ResponseEntity<List<ChargerResponse>> saveAllChargerByStatId(
            @RequestHeader(value = "pageNo", defaultValue = "1") Long pageNo,
            @RequestHeader(value = "numofRows", defaultValue = "10") Long numofRows,
            @RequestHeader(value = "zcode", defaultValue = "27") String zcode,
            @RequestHeader(value = "zscode", defaultValue = "27260") String zscode, // default: 대구스마트시티센터
            @PathVariable("statId") String statId
    ) throws IOException {
        List<ChargerResponse> chargerList = chargerInfoService.saveAllChargerByStatId(pageNo, numofRows, zcode, zscode, statId).getChargerList();

        return ResponseEntity.ok().body(chargerList);
    }



    @GetMapping()
    public ResponseEntity<List<ChargerResponse>> getAllChargerDetail(
            @RequestHeader(value = "pageNo", defaultValue = "1") Long pageNo,
            @RequestHeader(value = "numofRows", defaultValue = "10") Long numofRows,
            @RequestHeader(value = "zcode", defaultValue = "27") String zcode,
            @RequestHeader(value = "zscode", defaultValue = "27260") String zscode // default: 대구스마트시티센터
    ) throws IOException {
        chargerInfoService.saveAllCharger(pageNo, numofRows, zcode, zscode);

        List<ChargerEntity> chargerList = chargerService.getAllCharger(zcode, zscode);

        List<ChargerResponse> chargerResponseList = new ArrayList<>();

        for(ChargerEntity chargerEntity : chargerList){
            ChargerResponse chargerResponse = ChargerResponse.builder()
                    .uniqueChargerId(chargerEntity.getUniqueChargerId())
                    .stationName(chargerEntity.getStation().getName())
                    .stationId(chargerEntity.getStation().getChargingStationId())
                    .chargerId(chargerEntity.getChargerId())
                    .type(chargerEntity.getType())
                    .status(chargerEntity.getStatus())
                    .build();
            chargerResponseList.add(chargerResponse);
        }

        return ResponseEntity.ok().body(chargerResponseList);
    }

    @GetMapping("/{statId}")
    public ResponseEntity<List<ChargerResponse>> getAllChargerDetail(
            @RequestHeader(value = "pageNo", defaultValue = "1") Long pageNo,
            @RequestHeader(value = "numofRows", defaultValue = "10") Long numofRows,
            @RequestHeader(value = "zcode", defaultValue = "27") String zcode,
            @RequestHeader(value = "zscode", defaultValue = "27260") String zscode, // default: 대구스마트시티센터
            @PathVariable("statId") String statId
    ) throws IOException {
        chargerInfoService.saveAllChargerByStatId(pageNo, numofRows, zcode, zscode, statId);
        List<ChargerEntity> chargerList = chargerService.getAllChargerByStatId(statId);

        List<ChargerResponse> chargerResponseList = new ArrayList<>();

        for(ChargerEntity chargerEntity : chargerList){
            ChargerResponse chargerResponse = ChargerResponse.builder()
                    .uniqueChargerId(chargerEntity.getUniqueChargerId())
                    .stationName(chargerEntity.getStation().getName())
                    .stationId(chargerEntity.getStation().getChargingStationId())
                    .chargerId(chargerEntity.getChargerId())
                    .type(chargerEntity.getType())
                    .status(chargerEntity.getStatus())
                    .build();
            chargerResponseList.add(chargerResponse);
        }

        return ResponseEntity.ok().body(chargerResponseList);
    }

    @GetMapping("/{statId}/{chgerId}")
    public ResponseEntity<ChargerResponse> getAllChargerDetail(
            @RequestHeader(value = "pageNo", defaultValue = "1") Long pageNo,
            @RequestHeader(value = "numofRows", defaultValue = "10") Long numofRows,
            @RequestHeader(value = "zcode", defaultValue = "27") String zcode,
            @RequestHeader(value = "zscode", defaultValue = "27260") String zscode, // default: 대구스마트시티센터
            @PathVariable("statId") String statId,
            @PathVariable("chgerId") Long chgerId
    ) throws IOException {
        chargerInfoService.saveCharger(statId, chgerId);

        ChargerEntity chargerEntity = chargerService.getCharger(statId, chgerId);

        ChargerResponse chargerResponse = ChargerResponse.builder()
                .uniqueChargerId(chargerEntity.getUniqueChargerId())
                .stationName(chargerEntity.getStation().getName())
                .stationId(chargerEntity.getStation().getChargingStationId())
                .chargerId(chargerEntity.getChargerId())
                .type(chargerEntity.getType())
                .status(chargerEntity.getStatus())
                .build();

        return ResponseEntity.ok().body(chargerResponse);
    }
}



