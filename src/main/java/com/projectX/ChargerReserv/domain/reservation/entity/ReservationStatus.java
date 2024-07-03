package com.projectX.ChargerReserv.domain.reservation.entity;

/**
 * 예약 상태를 나타내는 열거형(enum)입니다.
 */
public enum ReservationStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    FAILED,
    COMPLETED
}
