package com.projectX.ChargerReserv.domain.user.repository;

import com.projectX.ChargerReserv.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByOuthId(Long outhId);
}
