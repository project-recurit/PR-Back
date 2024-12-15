package com.example.sideproject.global.notification.controller;

import com.example.sideproject.global.notification.dto.EventDto;
import com.example.sideproject.global.notification.service.SseService;
import com.example.sideproject.global.security.UserDetailsImpl;
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

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(sseService.connect(userDetails.getUser().getId()));
    }

    @DeleteMapping
    public ResponseEntity<Void> disconnect(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        sseService.disconnect(userDetails.getUser().getId());
        return ResponseEntity.ok().build();
    }
}
