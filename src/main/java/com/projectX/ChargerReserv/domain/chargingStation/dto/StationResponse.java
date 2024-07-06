package com.projectX.ChargerReserv.domain.chargingStation.dto;

import com.projectX.ChargerReserv.domain.chargingStation.entity.Business;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StationResponse {
    private String name;
    private Double latitude;
    private Double longitude;
    private String address;
    private String useTime;
    private Business busiId;
}
