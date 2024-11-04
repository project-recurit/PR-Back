package com.example.sideproject.domain.user.controller;

import com.example.sideproject.domain.user.dto.SignUpRequestDto;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody SignUpRequestDto requestDto) {
        userService.register(requestDto);
        return ResponseEntity.ok().build();
    }




}
