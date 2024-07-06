package com.projectX.ChargerReserv.domain.reservation.controller;

import com.projectX.ChargerReserv.domain.reservation.dto.CreateReservationRequest;
import com.projectX.ChargerReserv.domain.reservation.service.ReservationCreationService;
import com.projectX.ChargerReserv.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> createReservation(
            @Valid @RequestBody CreateReservationRequest request,
            Authentication authentication) {
        final Long userId = userService.getLoggedInUserId(authentication);
        final Long reservationId = reservationCreationService.createReservation(request.toCommand(userId));
        return ResponseEntity.created(URI.create("/api/v1/reservations/" + reservationId)).build();
    }
}
