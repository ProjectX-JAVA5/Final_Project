package com.projectX.ChargerReserv.externalapi.publicdata.dto.response;

import com.projectX.ChargerReserv.domain.charger.dto.response.ChargerResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChargerInfoResponse {
    private Long totalCount;
    private List<ChargerResponse> chargerList;
}
