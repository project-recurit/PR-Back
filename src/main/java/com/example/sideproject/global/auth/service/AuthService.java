package com.example.sideproject.global.auth.service;

import com.example.sideproject.domain.user.entity.User;

import com.example.sideproject.domain.user.entity.UserStatus;
import com.example.sideproject.domain.user.repository.UserRepository;
import com.example.sideproject.global.auth.dto.LoginRequestDto;
import com.example.sideproject.global.auth.dto.LoginResponseDto;
import com.example.sideproject.global.auth.dto.TokenResponseDto;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;



@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService  {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getId())
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));

        validateUserStatus(user);
        validatePassword(requestDto.getPassword(), user.getPassword());

        String accessToken = tokenService.createAccessToken(
                user.getUsername(),
                user.getUserStatus(),
                user.getUserRole()
        );
        String refreshToken = tokenService.createRefreshToken();

        updateUserLoginInfo(user, refreshToken);

        log.info("User logged in successfully: {}", user.getUsername());

        return LoginResponseDto.builder()
                .loginId(user.getUsername())
                .userRole(user.getUserRole())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void logout(String token) {
        try {
            if (token.startsWith(JwtTokenHelper.BEARER_PREFIX)) {
                token = token.substring(JwtTokenHelper.BEARER_PREFIX.length());
            }
            
            String username = tokenService.getUserIdFromToken(token);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));

            user.logout();
            userRepository.save(user);
            tokenService.invalidateToken(token);

            log.info("User logged out successfully: {}", username);
        } catch (Exception e) {
            log.error("Logout failed: {}", e.getMessage());
            throw new CustomException(ErrorType.LOGOUT_FAILED);
        }
    }

    public TokenResponseDto refreshToken(String accessToken, String refreshToken) {
        try {
            String userId = tokenService.getUserIdFromToken(accessToken);
            User user = userRepository.findByUsername(userId)
                    .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));

            if (!refreshToken.equals(user.getRefreshToken())) {
                throw new CustomException(ErrorType.INVALID_REFRESH_TOKEN);
            }

            // 기존 액세스 토큰 무효화
            tokenService.invalidateToken(accessToken);

            // 새로운 토큰 발급
            String newAccessToken = tokenService.createAccessToken(
                    user.getUsername(),
                    user.getUserStatus(),
                    user.getUserRole()
            );
            String newRefreshToken = tokenService.createRefreshToken();

            updateUserLoginInfo(user, newRefreshToken);

            log.info("Token refreshed successfully for user: {}", userId);

            return new TokenResponseDto(newAccessToken, newRefreshToken);
        } catch (Exception e) {
            log.error("Token refresh failed: {}", e.getMessage());
            throw new CustomException(ErrorType.TOKEN_REFRESH_FAILED);
        }
    }

    private void updateUserLoginInfo(User user, String refreshToken) {
        user.updateLoginInfo(refreshToken, LocalDateTime.now());
        userRepository.save(user);
    }

    private void validateUserStatus(User user) {
        if (user.getUserStatus() == UserStatus.INACTIVE_USER) {
            throw new CustomException(ErrorType.WITHDRAW_USER);
        }
        if (user.getUserStatus() == UserStatus.WITHDRAW_USER) {
            throw new CustomException(ErrorType.WITHDRAW_USER);
        }
    }

    private void validatePassword(String requestPassword, String userPassword) {
        if (!passwordEncoder.matches(requestPassword, userPassword)) {
            throw new CustomException(ErrorType.MISMATCH_PASSWORD);
        }
    }
}
