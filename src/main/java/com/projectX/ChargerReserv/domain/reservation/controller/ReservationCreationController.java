package com.projectX.ChargerReserv.domain.reservation.controller;

import com.projectX.ChargerReserv.domain.reservation.dto.CreateReservationRequest;
import com.projectX.ChargerReserv.domain.reservation.service.ReservationCreationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/reservations")
@AllArgsConstructor
public class ReservationCreationController {

    private final ReservationCreationService reservationCreationService;

    @PostMapping
    public ResponseEntity<Void> createReservation(
            @Valid @RequestBody CreateReservationRequest request) {
        Long userId = 1L; // TODO: 현재 로그인한 사용자 정보 가져오도록 수정
        final Long reservationId = reservationCreationService.createReservation(request.toCommand(userId));
        return ResponseEntity.created(URI.create("/api/v1/reservations/" + reservationId)).build();
    }
}
