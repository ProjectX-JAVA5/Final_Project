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

    @Version
    private Long version;

    public void reserve() {
        if (this.status == ChargerStatus.AVAILABLE) {
            this.status = ChargerStatus.RESERVED;
        } else {
            throw new IllegalStateException("사용할 수 없는 충전기입니다.");
        }
    }

    public void start() {
        if (this.status == ChargerStatus.RESERVED) {
            this.status = ChargerStatus.CHARGING;
        } else {
            throw new IllegalStateException("충전을 시작할 수 없는 상태입니다.");
        }
    }

    public void cancel() {
        if (this.status == ChargerStatus.RESERVED) {
            this.status = ChargerStatus.AVAILABLE;
        } else {
            throw new IllegalStateException("예약을 취소할 수 없는 상태입니다.");
        }
    }

    public void complete() {
        if (this.status == ChargerStatus.CHARGING) {
            this.status = ChargerStatus.AVAILABLE;
        } else {
            throw new IllegalStateException("충전을 완료할 수 없는 상태입니다.");
        }
    }
}
