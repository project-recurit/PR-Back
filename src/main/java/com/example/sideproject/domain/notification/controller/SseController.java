package com.example.sideproject.domain.notification.controller;

import com.example.sideproject.domain.notification.dto.EventDto;
import com.example.sideproject.domain.notification.entity.NotificationType;
import com.example.sideproject.domain.notification.service.SseService;
import com.example.sideproject.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "SSE API", description = "SSE 연결 및 연결 해제 API")
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

    @GetMapping("/sse-data")
    @Operation(summary = "sse 데이터 전송 데이터 확인 API", description = "sse 데이터 전송 데이터 확인 API, 실제 API가 아님")
    public EventDto sseData() {
        return EventDto.builder()
                .to(1L)
                .from(2L)
                .title("")
                .msg("알림 메시지 전송")
                .type(NotificationType.APPLICATION_RESULT)
                .needToPush(false)
                .pushAllowed(true)
                .relatedId(1L)
                .build();
    }
}
