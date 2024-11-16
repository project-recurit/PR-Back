package com.example.sideproject.global.auth.service;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.entity.UserRole;
import com.example.sideproject.domain.user.entity.UserStatus;
import com.example.sideproject.domain.user.repository.UserRepository;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtTokenHelper jwtTokenHelper;
    private final UserRepository userRepository;
    private final Map<String, LocalDateTime> blacklistedTokens = new ConcurrentHashMap<>();

    public String createAccessToken(String username, UserStatus status, UserRole role) {
        return jwtTokenHelper.createToken(username, status, role);
    }

    public String createRefreshToken() {
        return jwtTokenHelper.createRefreshToken();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtTokenHelper.getUserInfoFromToken(token);
        return createAuthentication(claims.getSubject());
    }

    private Authentication createAuthentication(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));

        // 토큰에서 추출한 role로 권한 생성
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getUserRole().getAuthority()));

        return new UsernamePasswordAuthenticationToken(
                username,  // principal
                null,     // credentials
                authorities  // authorities
        );
    }

    public String getUserIdFromToken(String token) {
        if (token.startsWith(JwtTokenHelper.BEARER_PREFIX)) {
            token = token.substring(JwtTokenHelper.BEARER_PREFIX.length());
        }
        Claims claims = jwtTokenHelper.getUserInfoFromToken(token);
        return claims.getSubject();
    }

    public UserRole getUserRoleFromToken(String token) {
        validateToken(token);
        Claims claims = jwtTokenHelper.getUserInfoFromToken(token);
        return UserRole.valueOf(claims.get("auth").toString());
    }

    public void invalidateToken(String token) {
        try {
            Claims claims = jwtTokenHelper.getUserInfoFromToken(token);
            LocalDateTime expirationTime = claims.getExpiration()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            blacklistedTokens.put(token, expirationTime);
            cleanupExpiredTokens();

            log.info("Token invalidated successfully");
        } catch (Exception e) {
            log.error("Token invalidation failed: {}", e.getMessage());
            throw new CustomException(ErrorType.TOKEN_INVALIDATION_ERROR);
        }
    }

    private boolean isTokenBlacklisted(String token) {
        cleanupExpiredTokens();
        return blacklistedTokens.containsKey(token);
    }

    private void cleanupExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        blacklistedTokens.entrySet().removeIf(entry -> entry.getValue().isBefore(now));
    }

    public void validateToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new CustomException(ErrorType.INVALID_TOKEN);
        }
        
        if (!jwtTokenHelper.validateToken(token)) {
            throw new CustomException(ErrorType.INVALID_TOKEN);
        }
        
        if (isTokenBlacklisted(token)) {
            throw new CustomException(ErrorType.INVALID_TOKEN);
        }
    }

}
