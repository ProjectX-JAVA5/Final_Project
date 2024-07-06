package com.projectX.ChargerReserv.externalapi.publicdata.dto.response;

import com.projectX.ChargerReserv.domain.chargingStation.dto.StationResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StationInfoResponse {
    public Long totalCount;
    public List<StationResponse> stationList;
}
