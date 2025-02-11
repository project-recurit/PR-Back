package com.example.sideproject.domain.user.controller;

import com.example.sideproject.domain.user.dto.*;
import com.example.sideproject.domain.user.service.UserService;
import com.example.sideproject.global.dto.ResponseDataDto;
import com.example.sideproject.global.dto.ResponseMessageDto;
import com.example.sideproject.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.sideproject.global.enums.ResponseStatus;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDataDto<UpdateRegisterResponseDto>> register(@RequestBody UpdateRegisterRequestDto requestDto) {
        UpdateRegisterResponseDto responseDto = userService.updateRegister(requestDto);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SIGNUP_SUCCESS, responseDto));
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<ResponseMessageDto> withdrawUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.withdrawUser(userDetails.getUser().getId());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.WITHDRAW_SUCCESS));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDataDto<ProfileResponseDto>> findUser(@PathVariable Long userId) {
        ProfileResponseDto profileResponse = userService.findUserDetail(userId);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.GET_USER_SUCCESS, profileResponse));
    }

    @PatchMapping("/my-page/tech-stack")
    public ResponseEntity<ResponseMessageDto> updateUserStack(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UpdateTechStackRequest requestDto) {
        userService.updateUserStack(userDetails.getUsername(), requestDto.getUserTechStacks());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.PROFILE_UPDATE));
    }

    @PatchMapping("/user-details")
    public ResponseEntity<ResponseMessageDto> updateUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody ProfileRequestDto requestDto) {
        userService.updateUserDetails(userDetails.getUser().getId(), requestDto);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.PROFILE_UPDATE));
    }
}
