package com.example.sideproject.global.auth.controller;

import com.example.sideproject.global.auth.dto.LoginRequestDto;
import com.example.sideproject.global.auth.dto.LoginResponseDto;
import com.example.sideproject.global.auth.dto.TokenResponseDto;
import com.example.sideproject.global.auth.service.AuthService;
import com.example.sideproject.global.auth.service.JwtTokenHelper;
import com.example.sideproject.global.dto.ResponseDataDto;
import com.example.sideproject.global.dto.ResponseMessageDto;
import com.example.sideproject.global.enums.ResponseStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDataDto<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequestDto requestDto) {
        LoginResponseDto response = authService.login(requestDto);
        return ResponseEntity.ok(new ResponseDataDto<>(
                ResponseStatus.LOGIN_SUCCESS,
                response
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseMessageDto> logout(
            @RequestHeader(JwtTokenHelper.AUTHORIZATION_HEADER) String token) {
        authService.logout(token);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.LOGOUT_SUCCESS));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseDataDto<TokenResponseDto>> refreshToken(
            @RequestHeader(JwtTokenHelper.AUTHORIZATION_HEADER) String accessToken,
            @RequestHeader(JwtTokenHelper.REFRESH_TOKEN_HEADER) String refreshToken) {
        TokenResponseDto tokenResponse = authService.refreshToken(accessToken, refreshToken);
        return ResponseEntity.ok(new ResponseDataDto<>(
                ResponseStatus.SUCCESS,
                tokenResponse
        ));
    }
}