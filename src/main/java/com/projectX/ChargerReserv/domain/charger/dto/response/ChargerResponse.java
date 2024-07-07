package com.projectX.ChargerReserv.domain.charger.dto.response;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerStatus;
import com.projectX.ChargerReserv.domain.charger.entity.ChargerType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChargerResponse {
    private Long uniqueChargerId;
    private String stationName;
    private String stationId;
    private Long chargerId;
    private ChargerType type;
    private ChargerStatus status;
}
