package com.example.sideproject.domain.user.controller;

import com.example.sideproject.domain.user.dto.*;
import com.example.sideproject.domain.user.service.UserService;
import com.example.sideproject.global.dto.ResponseDataDto;
import com.example.sideproject.global.dto.ResponseMessageDto;
import com.example.sideproject.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.sideproject.global.enums.ResponseStatus;

@Tag(name = "유저관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입 추가정보 업데이트", description = "첫 회원가입 시 추가정보 받는 api")
    @PostMapping("/register")
    public ResponseEntity<ResponseDataDto<UpdateRegisterResponseDto>> register(@RequestBody UpdateRegisterRequestDto requestDto) {
        UpdateRegisterResponseDto responseDto = userService.updateRegister(requestDto);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SIGNUP_SUCCESS, responseDto));
    }

    @Operation(summary = "회원탈퇴", description = "존재하지 않는 유저일 시 404에러")
    @DeleteMapping("/withdraw")
    public ResponseEntity<ResponseMessageDto> withdrawUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.withdrawUser(userDetails.getUser().getId());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.WITHDRAW_SUCCESS));
    }

    @Operation(summary = "회원조회", description = "존재하지 않는 유저일 시 404에러")
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDataDto<ProfileResponseDto>> findUser(@PathVariable Long userId) {
        ProfileResponseDto profileResponse = userService.findUserDetail(userId);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.GET_USER_SUCCESS, profileResponse));
    }

    @Operation(summary = "기술스택 업데이트", description = "존재하지 않는 유저일 시 404에러")
    @PatchMapping("/my-page/tech-stack")
    public ResponseEntity<ResponseMessageDto> updateUserStack(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UpdateTechStackRequest requestDto) {
        userService.updateUserStack(userDetails.getUsername(), requestDto.getUserTechStacks());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.PROFILE_UPDATE));
    }

    @Operation(summary = "회원정보 수정", description = "존재하지 않는 유저일 시 404에러")
    @PatchMapping("/user-details")
    public ResponseEntity<ResponseMessageDto> updateUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody ProfileRequestDto requestDto) {
        userService.updateUserDetails(userDetails.getUser().getId(), requestDto);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.PROFILE_UPDATE));
    }
}
