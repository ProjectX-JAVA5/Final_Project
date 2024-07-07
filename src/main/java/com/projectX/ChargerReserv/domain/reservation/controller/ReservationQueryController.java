package com.projectX.ChargerReserv.domain.reservation.controller;

import com.projectX.ChargerReserv.domain.reservation.dto.command.ReservationDetailQueryCommand;
import com.projectX.ChargerReserv.domain.reservation.dto.command.ReservationQueryCommand;
import com.projectX.ChargerReserv.domain.reservation.dto.response.ReservationDetailResponse;
import com.projectX.ChargerReserv.domain.reservation.dto.response.ReservationListResponse;
import com.projectX.ChargerReserv.domain.reservation.service.ReservationQueryService;
import com.projectX.ChargerReserv.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationQueryController {

    private final ReservationQueryService reservationQueryService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<ReservationListResponse>> getReservations(
            Authentication authentication,
            Pageable pageable,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Long userId = userService.getLoggedInUserId(authentication);
        ReservationQueryCommand command = ReservationQueryCommand.builder()
                .userId(userId)
                .startDate(startDate)
                .endDate(endDate)
                .pageable(pageable)
                .build();
        Page<ReservationListResponse> reservations = reservationQueryService.getReservations(command);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationDetailResponse> getReservationDetail(
            @PathVariable Long reservationId,
            Authentication authentication
    ) {
        Long userId = userService.getLoggedInUserId(authentication);
        ReservationDetailQueryCommand command = new ReservationDetailQueryCommand(reservationId, userId);
        ReservationDetailResponse reservation = reservationQueryService.getReservationDetail(command);
        return ResponseEntity.ok(reservation);
    }
}
