package com.projectX.ChargerReserv.domain.charger.entity;

import com.projectX.ChargerReserv.domain.chargingStation.entity.ChargingStationEntity;
import com.projectX.ChargerReserv.domain.chargingStation.entity.RegionDetail;
import com.projectX.ChargerReserv.domain.chargingStation.entity.StatRegion;
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

    @Column(name = "chger_type")
    private ChargerType type;

    @Column(name = "chger_status")
    private ChargerStatus status;

    private StatRegion zcode;

    private RegionDetail zscode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="station_id")
    private ChargingStationEntity station;

    @Version
    private Long version;

    public void available() {
        this.status = ChargerStatus.AVAILABLE;
    }

    public void charging() {
        this.status = ChargerStatus.CHARGING;
    }

    public void reserved() {
        this.status = ChargerStatus.RESERVED;
    }
}
