package com.projectX.ChargerReserv.domain.reservation.dto.request;

import com.projectX.ChargerReserv.domain.reservation.dto.command.CreateReservationCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateReservationRequest(
        @NotBlank(message = "차량 번호는 필수입니다.") String vehicleNumber,
        @NotNull(message = "충전기 ID는 필수입니다.") Long chargerId
) {

    public CreateReservationCommand toCommand(Long userId) {
        return CreateReservationCommand.builder()
                .userId(userId)
                .vehicleNumber(vehicleNumber)
                .chargerId(chargerId)
                .build();
    }
}
