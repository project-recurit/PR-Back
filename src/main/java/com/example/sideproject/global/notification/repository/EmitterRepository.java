package com.example.sideproject.global.notification.repository;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

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
            disconnect(id);
        });

        sseEmitter.onError((ex) -> {
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
