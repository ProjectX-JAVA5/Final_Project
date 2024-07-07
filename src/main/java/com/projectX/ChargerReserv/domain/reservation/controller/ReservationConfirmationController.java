package com.projectX.ChargerReserv.domain.reservation.controller;

import com.projectX.ChargerReserv.domain.reservation.dto.request.ConfirmReservationRequest;
import com.projectX.ChargerReserv.domain.reservation.service.ReservationConfirmationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationConfirmationController {

    private final ReservationConfirmationService reservationConfirmationService;

    @PatchMapping("/confirm")
    public ResponseEntity<Void> confirmReservation(
            @Valid @RequestBody ConfirmReservationRequest request) {
        reservationConfirmationService.confirmReservation(request.toCommand());
        return ResponseEntity.ok().build();
    }
}
