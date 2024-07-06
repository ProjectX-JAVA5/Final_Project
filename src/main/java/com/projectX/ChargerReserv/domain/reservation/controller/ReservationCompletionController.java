package com.projectX.ChargerReserv.domain.reservation.controller;

import com.projectX.ChargerReserv.domain.reservation.service.ReservationCompletionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationCompletionController {

    private final ReservationCompletionService reservationCompletionService;

    @PatchMapping("/{reservationId}/complete")
    public ResponseEntity<Void> completeReservation(
            @PathVariable Long reservationId) {
        reservationCompletionService.completeReservation(reservationId);
        return ResponseEntity.noContent().build();
    }
}
