package com.example.sideproject.global.auth.service;

import com.example.sideproject.domain.user.entity.UserRole;
import com.example.sideproject.domain.user.entity.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtTokenHelperTest {
    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Test
    void createToken() {
        String socialId = "123";
        UserStatus status = UserStatus.ACTIVE_USER;
        UserRole role = UserRole.USER;

        String token = jwtTokenHelper.createToken(socialId, status, role);
        System.out.println(token);
    }
}