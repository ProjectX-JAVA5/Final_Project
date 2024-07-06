package com.projectX.ChargerReserv.domain.reservation.controller;

import com.projectX.ChargerReserv.domain.reservation.dto.CancelReservationCommand;
import com.projectX.ChargerReserv.domain.reservation.service.ReservationCancellationService;
import com.projectX.ChargerReserv.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationCancellationController {

    private final ReservationCancellationService reservationCancellationService;
    private final UserService userService;

    @PatchMapping("/{reservationId}/cancel")
    public ResponseEntity<Void> cancelReservation(
            @PathVariable Long reservationId,
            Authentication authentication) {
        Long userId = userService.getLoggedInUserId(authentication);
        reservationCancellationService.cancelReservation(new CancelReservationCommand(reservationId, userId));
        return ResponseEntity.noContent().build();
    }
}
