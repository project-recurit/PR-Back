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
        return emitterMap.computeIfAbsent(id, key -> new SseEmitter());
    }

    public void disconnect(Long id) {
        emitterMap.remove(id);
    }

    public Optional<SseEmitter> getSession(Long id) {
        return Optional.ofNullable(emitterMap.get(id));
    }
}
