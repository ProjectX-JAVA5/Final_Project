package com.projectX.ChargerReserv.externalapi.publicdata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectX.ChargerReserv.domain.chargingStation.dto.StationResponse;
import com.projectX.ChargerReserv.domain.chargingStation.entity.Business;
import com.projectX.ChargerReserv.domain.chargingStation.entity.ChargingStationEntity;
import com.projectX.ChargerReserv.domain.chargingStation.entity.RegionDetail;
import com.projectX.ChargerReserv.domain.chargingStation.entity.StatRegion;
import com.projectX.ChargerReserv.domain.chargingStation.service.StationService;
import com.projectX.ChargerReserv.externalapi.publicdata.config.PublicDataConfig;
import com.projectX.ChargerReserv.externalapi.publicdata.dto.response.StationInfoResponse;
import com.projectX.ChargerReserv.global.error.CustomExceptionHandler;
import com.projectX.ChargerReserv.global.error.IllegalArgumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class StationInfoServiceImpl implements StationInfoService{

    private final PublicDataConfig publicDataConfig;
    private final StationService stationService;

    @Value("${PUBLIC-DATA.API-URL}")
    public String apiUrl;

    @Value("${PUBLIC-DATA.API-KEY}")
    public String apiKey;

    @Override
    public StationInfoResponse saveAllStationInfoByzcode(String Inputzcode, Long rows, Long pageNo) throws IOException {

        Map<String, Object> result = new HashMap<>();

        StringBuilder urlBuilder = new StringBuilder(apiUrl + "/getChargerInfo"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + apiKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(pageNo.toString(), "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(rows.toString(), "UTF-8")); /*한 페이지 결과 수 (최소 10, 최대 9999)*/
        urlBuilder.append("&" + URLEncoder.encode("zcode","UTF-8") + "=" + URLEncoder.encode(Inputzcode.toString(), "UTF-8")); /*시도 코드 (행정구역코드 앞 2자리)*/
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

        JSONObject StationJson = XML.toJSONObject(sb.toString());
        String jsonPrettyPrintString = StationJson.toString(4);

        System.out.println("API response:");
        System.out.println(jsonPrettyPrintString);

        ObjectMapper objectMapper = new ObjectMapper();
        List<StationResponse> stationResponseList = new ArrayList<>();

        StationInfoResponse stationInfoResponse = StationInfoResponse.builder().build();

        try {
            JsonNode rootNode = objectMapper.readTree(StationJson.toString());
            JsonNode headerNode = rootNode.path("response").path("header");
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            stationInfoResponse.setTotalCount(headerNode.path("totalCount").asLong());

            if (itemsNode.isArray()) {
                for (JsonNode itemNode : itemsNode) {
                    String statId = itemNode.path("statId").asText();
                    String zcode = itemNode.path("zcode").asText();
                    String busiId = itemNode.path("busiId").asText();
                    String zscode = itemNode.path("zscode").asText();
                    String useTime = itemNode.path("useTime").asText();
                    String addr = itemNode.path("addr").asText();
                    Double lat = itemNode.path("lat").asDouble();
                    Double lng = itemNode.path("lng").asDouble();
                    String statNm = itemNode.path("statNm").asText();

                    StationResponse stationResponse = StationResponse.builder()
                            .name(statNm)
                            .address(addr)
                            .busiId(Business.fromCode(busiId))
                            .useTime(useTime)
                            .latitude(lat)
                            .longitude(lng)
                            .build();

                    ChargingStationEntity chargingStation = ChargingStationEntity.builder()
                            .chargingStationId(statId)
                            .name(statNm)
                            .latitude(lat)
                            .longitude(lng)
                            .address(addr)
                            .zcode(StatRegion.fromCode(zcode))
                            .zscode(RegionDetail.fromCode(zscode))
                            .useTime(useTime)
                            .busiId(Business.fromCode(busiId))
                            .build();
                    try {
                        stationService.saveStation(chargingStation);
                        System.out.println("Save chargingStation : " + chargingStation.toString());

                        stationResponseList.add(stationResponse);
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("충전소 데이터가 형식에 맞지 않습니다.");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        stationInfoResponse.setStationList(stationResponseList);

        return stationInfoResponse;
    }
}
