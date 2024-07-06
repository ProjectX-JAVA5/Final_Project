package com.projectX.ChargerReserv.domain.chargingStation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StationRequest {
    private Double latitude;
    private Double longgitude;
}
