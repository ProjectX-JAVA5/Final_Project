package com.projectX.ChargerReserv.domain.user.repository;

import com.projectX.ChargerReserv.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByOuthId(Long outhId);
}