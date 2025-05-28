package com.example.sideproject.domain.fcm.controller;

import com.example.sideproject.domain.fcm.dto.FcmTokenRequest;
import com.example.sideproject.domain.fcm.dto.payload.NotificationMessage;
import com.example.sideproject.domain.fcm.service.FcmNotificationSender;
import com.example.sideproject.domain.fcm.service.FcmService;
import com.example.sideproject.global.dto.ResponseDataDto;
import com.example.sideproject.global.dto.ResponseMessageDto;
import com.example.sideproject.global.enums.ResponseStatus;
import com.example.sideproject.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "fcm token 관리 api", description = "fcm token 관리 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/fcm")
public class FcmController {
    private final FcmService fcmService;
    private final FcmNotificationSender fcmNotificationSender;

    @Operation(summary = "fcm token 조회", description = "유저의 fcm token을 조회한다.")
    @GetMapping
    public ResponseEntity<ResponseDataDto<List<String>>> getTokens(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<String> tokens = fcmService.getTokens(userDetails.getUser());
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, tokens));
    }

    @Operation(summary = "fcm token 저장", description = "id에 동일한 토큰이 있으면 401 에러 발생")
    @PostMapping
    public ResponseEntity<ResponseMessageDto> saveToken(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestBody FcmTokenRequest req) {
        fcmService.saveToken(userDetails.getUser(), req.token());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.SUCCESS));
    }

    @Operation(summary = "fcm token 삭제", description = "해당 토큰이 없으면 401 에러 발생")
    @DeleteMapping
    public ResponseEntity<ResponseMessageDto> deleteToken(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                          String token) {
        fcmService.deleteToken(userDetails.getUser(), token);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.SUCCESS));
    }

    @Operation(summary = "push 메시지 전송", description = "[관리자] 푸시 메시지를 전송합니다. userId를 검증하지 않습니다.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/direct")
    public void sendDirectExchange(@RequestBody NotificationMessage notificationMessage) {
        fcmNotificationSender.send(notificationMessage);
    }
}
