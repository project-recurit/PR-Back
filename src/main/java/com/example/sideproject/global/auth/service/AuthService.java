package com.example.sideproject.global.auth.service;

import com.example.sideproject.domain.user.dto.SignUpRequestDto;
import com.example.sideproject.domain.user.dto.SignUpResponseDto;
import com.example.sideproject.domain.user.entity.User;

import com.example.sideproject.domain.user.entity.UserStatus;
import com.example.sideproject.domain.user.repository.UserRepository;
import com.example.sideproject.domain.user.service.UserService;
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
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService  {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public LoginResponseDto login(LoginRequestDto requestDto) {
        Optional<User> optionalUser = userRepository.findBySocialId(requestDto.getSocialId());

        User user;
        if (optionalUser.isEmpty()) {
            SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                    .socialId(requestDto.getSocialId())
                    .socialProvider(requestDto.getProvider())
                    .email(requestDto.getEmail())
                    .nickname(requestDto.getNickname())
                    // 소셜 로그인의 경우 필요한 기본값 설정
                    .username(requestDto.getSocialId()) // 소셜 ID를 username으로 사용
                    .password(passwordEncoder.encode(requestDto.getSocialId())) // 임시 비밀번호
                    .techStack1s(requestDto.getTechStack1s()) // 빈 기술 스택으로 시작
                    .build();

            SignUpResponseDto signUpResponse = userService.register(signUpRequestDto);
            user = userRepository.findBySocialId(signUpResponse.getUsername())
                    .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));
        }

        else {
            user = optionalUser.get();
            validateUserStatus(user);
        }

        String accessToken = tokenService.createAccessToken(
                user.getSocialId(),
                user.getUserStatus(),
                user.getUserRole()
        );
        String refreshToken = tokenService.createRefreshToken();

        updateUserLoginInfo(user, refreshToken);

        log.info("User logged in successfully: {}", user.getSocialId());

        return LoginResponseDto.builder()
                .loginId(user.getSocialId())
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

            String socialId = tokenService.getUserIdFromToken(token);
            User user = userRepository.findBySocialId(socialId)
                    .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));

            user.logout();
            userRepository.save(user);
            tokenService.invalidateToken(token);

            log.info("User logged out successfully: {}", socialId);
        } catch (Exception e) {
            log.error("Logout failed: {}", e.getMessage());
            throw new CustomException(ErrorType.LOGOUT_FAILED);
        }
    }

    public TokenResponseDto refreshToken(String accessToken, String refreshToken) {
        try {
            String socialId = tokenService.getUserIdFromToken(accessToken);
            User user = userRepository.findBySocialId(socialId)
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

            log.info("Token refreshed successfully for user: {}", socialId);

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


}
