package com.projectX.ChargerReserv.domain.chargingStation.entity;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerEntity;
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
@Table(name = "chargingStation")
public class ChargingStationEntity extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chargingStationId;

    private String name;

    private Long latitude;

    private Long longitude;

    private String address;

    private StatRegion zcode;

    private RegionDetail zscode;

    private String useTime;

    private Business busiId;

    @OneToMany(mappedBy = "station", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ChargerEntity> chargerEntityList;
}
