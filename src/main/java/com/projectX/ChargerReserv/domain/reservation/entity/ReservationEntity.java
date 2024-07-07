package com.projectX.ChargerReserv.domain.reservation.entity;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerEntity;
import com.projectX.ChargerReserv.domain.user.entity.UserEntity;
import com.projectX.ChargerReserv.global.basic.BasicEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "reservation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationEntity extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @Column(name = "reservation_number", nullable = false, unique = true)
    private String reservationNumber;

    @Column(name = "vehicle_number", nullable = false)
    private String vehicleNumber;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Column(name = "charger_start_at")
    private LocalDateTime chargerStartAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charger_id", nullable = false)
    private ChargerEntity charger;

    @Builder
    public ReservationEntity(
            String reservationNumber,
            String vehicleNumber,
            LocalDateTime startAt,
            LocalDateTime endAt,
            ReservationStatus status,
            UserEntity user,
            ChargerEntity charger
    ) {
        this.reservationNumber = reservationNumber;
        this.vehicleNumber = vehicleNumber;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
        this.user = user;
        this.charger = charger;
    }

    public void confirm() {
        this.status = ReservationStatus.CONFIRMED;
        this.chargerStartAt = LocalDateTime.now();
    }

    public void failed() {
        this.status = ReservationStatus.FAILED;
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }

    public void complete() {
        this.status = ReservationStatus.COMPLETED;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.endAt);
    }
}
