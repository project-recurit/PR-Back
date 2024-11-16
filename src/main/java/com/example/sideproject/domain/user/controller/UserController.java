package com.example.sideproject.domain.user.controller;

import com.example.sideproject.domain.user.dto.ProfileRequestDto;
import com.example.sideproject.domain.user.dto.ProfileResponseDto;
import com.example.sideproject.domain.user.dto.SignUpRequestDto;
import com.example.sideproject.domain.user.dto.UpdateTechStackRequest;
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
        return ResponseEntity.ok(requestDto.getUsername());
    }

    //Todo 추후 비밀번호 검증 추가
    @PutMapping("/{userId}/withdraw")
    public ResponseEntity withdrawUser(@PathVariable Long userId) {
        userService.withdrawUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponseDto> findUser(@PathVariable Long userId) {
        ProfileResponseDto profileResponse = userService.findUserDetail(userId);
        return ResponseEntity.ok(profileResponse);
    }

    @PatchMapping("/my-page/tech-stack")
    public ResponseEntity updateUserStack(@PathVariable String username, @RequestBody UpdateTechStackRequest requestDto){
        userService.updateUserStack(username, requestDto.getTechStacks());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}/user-details")
    public ResponseEntity updateUser(@PathVariable Long userId, @RequestBody ProfileRequestDto requestDto){
        userService.updateUserDetails(userId, requestDto);
        return ResponseEntity.ok().build();
    }


}
