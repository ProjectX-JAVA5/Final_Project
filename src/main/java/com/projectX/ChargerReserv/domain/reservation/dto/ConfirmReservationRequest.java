package com.projectX.ChargerReserv.domain.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ConfirmReservationRequest(
        @NotBlank(message = "차량 번호는 필수입니다.") String vehicleNumber,
        @NotNull(message = "충전기 ID는 필수입니다.") Long chargerId
) {

    public ConfirmReservationCommand toCommand() {
        return ConfirmReservationCommand.builder()
                .vehicleNumber(vehicleNumber)
                .chargerId(chargerId)
                .build();
    }
}
