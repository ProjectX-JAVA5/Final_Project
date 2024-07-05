package com.projectX.ChargerReserv.domain.user.controller;

import com.projectX.ChargerReserv.domain.user.dto.AuthLoginRes;
import com.projectX.ChargerReserv.domain.user.dto.UserInfo;
import com.projectX.ChargerReserv.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public ResponseEntity<AuthLoginRes> login(String code) throws IOException {
        return userService.login(code);
    }

    //Authorization 헤더에 RefreshToken
    @PatchMapping("/login")
    public ResponseEntity<AuthLoginRes> login(Authentication authentication){
        return userService.login(authentication);
    }

    //서버에 남은 RefreshToken 삭제
    @DeleteMapping("/login")
    public ResponseEntity<HttpStatus> logout(Authentication authentication){
        return userService.logout(authentication);
    }

    //현재 로그인한 유저정보 조회
    @GetMapping("/userInfo")
    public ResponseEntity<UserInfo> getUserInfo(Authentication authentication) throws IOException {
        return userService.getUserInfo(authentication);
    }


}
