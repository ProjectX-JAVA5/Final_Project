package com.projectX.ChargerReserv.domain.charger.repository;

import com.projectX.ChargerReserv.domain.charger.entity.ChargerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargerRepository extends JpaRepository<ChargerEntity, Long> {
}
