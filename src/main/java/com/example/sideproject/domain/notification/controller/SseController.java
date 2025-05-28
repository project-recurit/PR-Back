package com.example.sideproject.domain.notification.controller;

import com.example.sideproject.domain.notification.service.SseService;
import com.example.sideproject.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequestMapping("/api/v1/sse")
@RestController
@RequiredArgsConstructor
public class SseController {
    private final SseService sseService;

    @Operation(summary = "sse 연결", description = "sse에 연결합니다. 마지막 데이터 전송 이후 5분간 유지됩니다. 연결 해제 시 재 접속이 필요합니다.")
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return sseService.connect(userDetails.getUser().getId());
    }

    @Operation(summary = "sse 연결 해제", description = "sse 연결 해제")
    @DeleteMapping
    public ResponseEntity<Void> disconnect(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        sseService.disconnect(userDetails.getUser().getId());
        return ResponseEntity.ok().build();
    }
}
