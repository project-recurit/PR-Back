package com.example.sideproject.global.auth.service;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.entity.UserRole;
import com.example.sideproject.domain.user.entity.UserStatus;
import com.example.sideproject.domain.user.repository.UserRepository;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenHelper {

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // Refresh token
    public static final String REFRESH_TOKEN_HEADER = "Refresh_token";
    //
    public static  final String ACCESS_CONTROL_EXPOSE_HEADERS_HEADER = "Access-Control-Expose-Headers";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // 회원 상태의 값 KEY
    public static final String STATUS_KET = "status";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long TOKEN_TIME = 30 * 60 * 1000L; // 30분

    private final long REFRESH_TOKEN_TIME = 14 * 24 * 60 * 60 * 1000L;
    // ADMIN USER

    private final UserRepository userRepository;

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createToken(String username, UserStatus status, UserRole authority) {
        Date date = new Date();

        String token = Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, authority)
                .claim(STATUS_KET, status)
                .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();

        return BEARER_PREFIX + token;
    }

    public String createRefreshToken() {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setIssuedAt(date)
                        .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    // header 에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request, String tokenHeader) {
        String bearerToken = request.getHeader(tokenHeader);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            if (token.startsWith(BEARER_PREFIX)) {
                token = token.substring(BEARER_PREFIX.length());
            }
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        try {
            if (token.startsWith(BEARER_PREFIX)) {
                return getExpiredAccessToken(token);
            }
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Failed to get user info from token: {}", e.getMessage());
            throw new CustomException(ErrorType.INVALID_TOKEN);
        }
    }

    @Transactional
    public void loginDateAndSaveRefreshToken(String username, String refreshToken) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));
        user.saveRefreshToken(refreshToken);
        user.setLogin();
    }

    public Claims getExpiredAccessToken(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken.substring(7))
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
