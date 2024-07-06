package com.projectX.ChargerReserv.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfo {
    private Long userId;

    private String userName;

    private String email;

    private Long outhId;
}
