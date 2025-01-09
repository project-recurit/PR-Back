package com.example.sideproject.global.notification.service;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.notification.aop.annotation.NotifyOn;
import com.example.sideproject.global.notification.dto.EventDto;
import com.example.sideproject.global.notification.entity.NotificationType;
import com.example.sideproject.global.notification.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {
    private final EmitterRepository emitterRepository;

    public SseEmitter connect(Long userId) {
        log.debug("{} 알림 서비스 접속", userId);
        SseEmitter session = emitterRepository.connect(userId);

        EventDto data = new EventDto(userId, userId, "connect", NotificationType.CONNECT, "");
        send(userId, data, session);

        return session;
    }

    public void send(EventDto eventDto) {
        SseEmitter session = emitterRepository.getSession(eventDto.to())
                .orElse(null);

        if (session == null) {
            return;
        }

        log.debug("{}에게 알람 전송을 시작합니다.", eventDto.to());

        send(eventDto.to(), eventDto, session);
    }

    public void send(Long sseId, Object data, SseEmitter session) {
        try {
            session.send(SseEmitter.event()
                    .data(data)
                    .build());
        } catch (IOException e) {
            disconnect(sseId);
        }
    }

    public void disconnect(Long userId) {
        emitterRepository.disconnect(userId);
    }

}
