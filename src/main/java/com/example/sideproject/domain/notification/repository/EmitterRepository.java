package com.example.sideproject.domain.notification.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class EmitterRepository {
    private final Map<Long, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    public SseEmitter connect(Long id) {
        // 5분 동안 연결
        SseEmitter sseEmitter = emitterMap.computeIfAbsent(id, key -> new SseEmitter((long) (60000 * 5)));
        // sse 연결 완료 시 삭제
        sseEmitter.onCompletion(() -> {
            disconnect(id);
        });

        sseEmitter.onTimeout(() -> {
            log.warn("[SSE] Connection timeout for id: {}", id);
            disconnect(id);
        });

        sseEmitter.onError((ex) -> {
            log.error("[SSE] Connection error for id: {}", id, ex);
            disconnect(id);
        });

        return sseEmitter;
    }

    public void disconnect(Long id) {
        emitterMap.remove(id);
    }

    public Optional<SseEmitter> getSession(Long id) {
        return Optional.ofNullable(emitterMap.get(id));
    }
}
