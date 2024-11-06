package com.example.sideproject.domain.user.controller;

import com.example.sideproject.domain.user.dto.SignUpRequestDto;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //Todo 추후 비밀번호 검증 추가
    @PutMapping("/{userId}")
    public ResponseEntity withdrawUser(@PathVariable Long userId) {
        userService.withdrawUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity findUser(@PathVariable Long userId) {
        userService.findUserDetail(userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity updateUserStack(@PathVariable Long userId){
        userService.updateUserStack(userId);
        return ResponseEntity.ok().build();
    }



}
