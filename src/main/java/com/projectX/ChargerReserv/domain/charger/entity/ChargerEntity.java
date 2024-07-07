package com.projectX.ChargerReserv.domain.charger.entity;

import com.projectX.ChargerReserv.domain.chargingStation.entity.ChargingStationEntity;
import com.projectX.ChargerReserv.domain.reservation.entity.ReservationEntity;
import com.projectX.ChargerReserv.global.basic.BasicEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "charger")
public class ChargerEntity extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uniqueChargerId;

    private Long chargerId;

    private ChargerType type;

    private ChargerStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ChargingStation_id")
    private ChargingStationEntity station;
}
