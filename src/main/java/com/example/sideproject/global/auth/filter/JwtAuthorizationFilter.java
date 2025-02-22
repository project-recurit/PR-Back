package com.example.sideproject.global.auth.filter;

import com.example.sideproject.global.auth.service.JwtTokenHelper;
import com.example.sideproject.global.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenHelper jwtTokenHelper;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtTokenHelper jwtTokenHelper,
                                  UserDetailsServiceImpl userDetailsService) {
        this.jwtTokenHelper = jwtTokenHelper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
            throws ServletException, IOException {

        String accessValue = jwtTokenHelper.getJwtFromHeader(req,
                JwtTokenHelper.AUTHORIZATION_HEADER);
        String refreshValue = jwtTokenHelper.getJwtFromHeader(req,
                JwtTokenHelper.REFRESH_TOKEN_HEADER);

        if (StringUtils.hasText(refreshValue)) {
            if (!jwtTokenHelper.validateToken(refreshValue)) {
                log.error("RefreshToken Error");
                return;
            }
        }
        else if (StringUtils.hasText(accessValue)) {
            if (!jwtTokenHelper.validateToken(accessValue)) {
                log.error("AccessToken Error");
                return;
            }
            Claims info = jwtTokenHelper.getUserInfoFromToken(accessValue);

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.setCharacterEncoding("utf-8");
                res.getWriter().write("상태 : " + res.getStatus() + e.getMessage());
            }
        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String id) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(id);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
