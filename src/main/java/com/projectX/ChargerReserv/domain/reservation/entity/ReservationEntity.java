package com.projectX.ChargerReserv.domain.reservation.entity;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerEntity;
import com.projectX.ChargerReserv.domain.user.entity.UserEntity;
import com.projectX.ChargerReserv.global.basic.BasicEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reservation")
public class ReservationEntity extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @Column(name="vehicle_num")
    private String vehicleNumber;

    @Column(name="startTime")
    private LocalDateTime startTime;

    @Column(name="endTime")
    private LocalDateTime endTime;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Column(name="charger_startTime")
    private LocalDateTime chargerStartTime;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="user_id")
    private UserEntity user;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="charger_id")
    private ChargerEntity charger;
}
