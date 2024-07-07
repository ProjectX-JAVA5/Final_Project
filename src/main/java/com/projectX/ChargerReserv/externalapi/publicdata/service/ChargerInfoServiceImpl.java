package com.projectX.ChargerReserv.externalapi.publicdata.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectX.ChargerReserv.domain.charger.dto.response.ChargerResponse;
import com.projectX.ChargerReserv.domain.charger.entity.ChargerEntity;
import com.projectX.ChargerReserv.domain.charger.entity.ChargerStatus;
import com.projectX.ChargerReserv.domain.charger.entity.ChargerType;
import com.projectX.ChargerReserv.domain.charger.service.ChargerService;
import com.projectX.ChargerReserv.domain.chargingStation.dto.StationResponse;
import com.projectX.ChargerReserv.domain.chargingStation.entity.Business;
import com.projectX.ChargerReserv.domain.chargingStation.entity.ChargingStationEntity;
import com.projectX.ChargerReserv.domain.chargingStation.entity.RegionDetail;
import com.projectX.ChargerReserv.domain.chargingStation.entity.StatRegion;
import com.projectX.ChargerReserv.domain.chargingStation.repository.StationRepository;
import com.projectX.ChargerReserv.domain.chargingStation.service.StationService;
import com.projectX.ChargerReserv.externalapi.publicdata.dto.response.ChargerInfoResponse;
import com.projectX.ChargerReserv.externalapi.publicdata.dto.response.StationInfoResponse;
import com.projectX.ChargerReserv.global.error.IllegalArgumentException;
import com.projectX.ChargerReserv.global.error.NoExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChargerInfoServiceImpl implements ChargerInfoService{

    private final ChargerService chargerService;
    private final StationRepository stationRepository;

    @Value("${PUBLIC-DATA.API-URL}")
    public String apiUrl;

    @Value("${PUBLIC-DATA.API-KEY}")
    public String apiKey;

    @Override
    public ChargerInfoResponse saveAllCharger(Long pageNo, Long rows, String Inputzcode, String Inputzscode) throws IOException {

        StringBuilder urlBuilder = new StringBuilder(apiUrl + "/getChargerInfo"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + apiKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(pageNo.toString(), "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(rows.toString(), "UTF-8")); /*한 페이지 결과 수 (최소 10, 최대 9999)*/
        urlBuilder.append("&" + URLEncoder.encode("zcode","UTF-8") + "=" + URLEncoder.encode(Inputzcode.toString(), "UTF-8")); /*시도 코드 (행정구역코드 앞 2자리)*/
        urlBuilder.append("&" + URLEncoder.encode("zscode","UTF-8") + "=" + URLEncoder.encode(Inputzscode.toString(), "UTF-8"));
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        JSONObject ChargerJson = XML.toJSONObject(sb.toString());
        String jsonPrettyPrintString = ChargerJson.toString(4);

        System.out.println("API response:");
        System.out.println(jsonPrettyPrintString);

        ObjectMapper objectMapper = new ObjectMapper();
        List<ChargerResponse> chargerResponseList = new ArrayList<>();

        ChargerInfoResponse chargerInfoResponse = ChargerInfoResponse.builder().build();

        try {
            JsonNode rootNode = objectMapper.readTree(ChargerJson.toString());
            JsonNode headerNode = rootNode.path("response").path("header");
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            chargerInfoResponse.setTotalCount(headerNode.path("totalCount").asLong());

            if (itemsNode.isArray()) {
                for (JsonNode itemNode : itemsNode) {

                    String statNm = itemNode.path("statNm").asText();
                    String statId = itemNode.path("statId").asText();
                    Long chgerId = itemNode.path("chgerId").asLong();
                    ChargerType type = ChargerType.fromCode(itemNode.path("chgerType").asText());
                    ChargerStatus status = ChargerStatus.fromCode(itemNode.path("stat").asText());
                    String zcode = itemNode.path("zcode").asText();
                    String zscode = itemNode.path("zscode").asText();

                    ChargerResponse chargerResponse = ChargerResponse.builder()
                            .stationName(statNm)
                            .stationId(statId)
                            .chargerId(chgerId)
                            .type(type)
                            .status(status)
                            .build();

                    Optional<ChargingStationEntity> stationEntity = stationRepository.findById(statId);
                    if(stationEntity.isEmpty()){
                        throw new NoExistException("충전소 정보가 없습니다.");
                    }

                    ChargerEntity chargerEntity = ChargerEntity.builder()
                            .chargerId(chgerId)
                            .type(type)
                            .status(status)
                            .station(stationEntity.get())
                            .zcode(StatRegion.fromCode(zcode))
                            .zscode(RegionDetail.fromCode(zscode))
                            .build();

                    try {
                        System.out.println("Save Charger : " + chargerEntity.toString());
                        chargerService.saveCharger(chargerEntity);

                        chargerResponseList.add(chargerResponse);
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("충전기 데이터가 형식에 맞지 않습니다.");
                    }
                }
                System.out.println("Done save.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        chargerInfoResponse.setChargerList(chargerResponseList);

        return chargerInfoResponse;
    }

    @Override
    public ChargerInfoResponse saveAllChargerByStatId(Long pageNo, Long rows, String Inputzcode, String Inputzscode, String InputstatId) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(apiUrl + "/getChargerInfo"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + apiKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(pageNo.toString(), "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(rows.toString(), "UTF-8")); /*한 페이지 결과 수 (최소 10, 최대 9999)*/
        urlBuilder.append("&" + URLEncoder.encode("zcode","UTF-8") + "=" + URLEncoder.encode(Inputzcode.toString(), "UTF-8")); /*시도 코드 (행정구역코드 앞 2자리)*/
        urlBuilder.append("&" + URLEncoder.encode("zscode","UTF-8") + "=" + URLEncoder.encode(Inputzscode.toString(), "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("statId","UTF-8") + "=" + URLEncoder.encode(InputstatId.toString(), "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        JSONObject ChargerJson = XML.toJSONObject(sb.toString());
        String jsonPrettyPrintString = ChargerJson.toString(4);

        System.out.println("API response:");
        System.out.println(jsonPrettyPrintString);

        ObjectMapper objectMapper = new ObjectMapper();
        List<ChargerResponse> chargerResponseList = new ArrayList<>();

        ChargerInfoResponse chargerInfoResponse = ChargerInfoResponse.builder().build();

        try {
            JsonNode rootNode = objectMapper.readTree(ChargerJson.toString());
            JsonNode headerNode = rootNode.path("response").path("header");
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            chargerInfoResponse.setTotalCount(headerNode.path("totalCount").asLong());

            if (itemsNode.isArray()) {
                for (JsonNode itemNode : itemsNode) {
                    String statNm = itemNode.path("statNm").asText();
                    String statId = itemNode.path("statId").asText();
                    Long chgerId = itemNode.path("chgerId").asLong();
                    ChargerType type = ChargerType.fromCode(itemNode.path("chgerType").asText());
                    ChargerStatus status = ChargerStatus.fromCode(itemNode.path("stat").asText());
                    String zcode = itemNode.path("zcode").asText();
                    String zscode = itemNode.path("zscode").asText();

                    ChargerResponse chargerResponse = ChargerResponse.builder()
                            .stationName(statNm)
                            .stationId(statId)
                            .chargerId(chgerId)
                            .type(type)
                            .status(status)
                            .build();

                    Optional<ChargingStationEntity> stationEntity = stationRepository.findById(statId);
                    if(stationEntity.isEmpty()){
                        throw new NoExistException("충전소 정보가 없습니다.");
                    }

                    ChargerEntity chargerEntity = ChargerEntity.builder()
                            .chargerId(chgerId)
                            .type(type)
                            .status(status)
                            .station(stationEntity.get())
                            .zcode(StatRegion.fromCode(zcode))
                            .zscode(RegionDetail.fromCode(zscode))
                            .build();
                    try {
                        System.out.println("Save Charger : " + chargerEntity.toString());
                        chargerService.saveCharger(chargerEntity);

                        chargerResponseList.add(chargerResponse);
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("충전기 데이터가 형식에 맞지 않습니다.");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        chargerInfoResponse.setChargerList(chargerResponseList);

        return chargerInfoResponse;
    }

    @Override
    public ChargerInfoResponse saveCharger(String InputstatId, Long InputchgerId) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(apiUrl + "/getChargerInfo"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + apiKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*한 페이지 결과 수 (최소 10, 최대 9999)*/
        urlBuilder.append("&" + URLEncoder.encode("statId","UTF-8") + "=" + URLEncoder.encode(InputstatId.toString(), "UTF-8")); /*시도 코드 (행정구역코드 앞 2자리)*/
        urlBuilder.append("&" + URLEncoder.encode("chgerId","UTF-8") + "=" + URLEncoder.encode(InputchgerId.toString(), "UTF-8"));
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        JSONObject ChargerJson = XML.toJSONObject(sb.toString());
        String jsonPrettyPrintString = ChargerJson.toString(4);

        System.out.println("API response:");
        System.out.println(jsonPrettyPrintString);

        ObjectMapper objectMapper = new ObjectMapper();
        List<ChargerResponse> chargerResponseList = new ArrayList<>();

        ChargerInfoResponse chargerInfoResponse = ChargerInfoResponse.builder().build();

        try {
            JsonNode rootNode = objectMapper.readTree(ChargerJson.toString());
            JsonNode headerNode = rootNode.path("response").path("header");
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            chargerInfoResponse.setTotalCount(headerNode.path("totalCount").asLong());

            if (itemsNode.isArray()) {
                for (JsonNode itemNode : itemsNode) {
                    String statNm = itemNode.path("statNm").asText();
                    String statId = itemNode.path("statId").asText();
                    Long chgerId = itemNode.path("chgerId").asLong();
                    ChargerType type = ChargerType.fromCode(itemNode.path("chgerType").asText());
                    ChargerStatus status = ChargerStatus.fromCode(itemNode.path("stat").asText());
                    String zcode = itemNode.path("zcode").asText();
                    String zscode = itemNode.path("zscode").asText();

                    ChargerResponse chargerResponse = ChargerResponse.builder()
                            .stationName(statNm)
                            .stationId(statId)
                            .chargerId(chgerId)
                            .type(type)
                            .status(status)
                            .build();

                    Optional<ChargingStationEntity> stationEntity = stationRepository.findById(statId);
                    if(stationEntity.isEmpty()){
                        throw new NoExistException("충전소 정보가 없습니다.");
                    }

                    ChargerEntity chargerEntity = ChargerEntity.builder()
                            .chargerId(chgerId)
                            .type(type)
                            .status(status)
                            .station(stationEntity.get())
                            .zcode(StatRegion.fromCode(zcode))
                            .zscode(RegionDetail.fromCode(zscode))
                            .build();

                    try {
                        System.out.println("Save Charger : " + chargerEntity.toString());
                        chargerService.saveCharger(chargerEntity);

                        chargerResponseList.add(chargerResponse);
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("충전기 데이터가 형식에 맞지 않습니다.");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        chargerInfoResponse.setChargerList(chargerResponseList);

        return chargerInfoResponse;
    }
}
