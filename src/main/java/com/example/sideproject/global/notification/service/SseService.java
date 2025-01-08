package com.example.sideproject.global.notification.service;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.notification.aop.annotation.NotifyOn;
import com.example.sideproject.global.notification.dto.EventDto;
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
        log.info("{} 알림 서비스 접속", userId);
        return emitterRepository.connect(userId);
    }

    public void send(EventDto eventDto) {
        log.info("{}에게 알람 전송을 시작합니다.", eventDto.to());
        SseEmitter session = emitterRepository.getSession(eventDto.to())
                .orElseThrow(RuntimeException::new);
        try {
            session.send(SseEmitter.event()
                    .data(eventDto)
                    .build());
        } catch (IOException e) {
            log.info("알림 메시지 전송 중 오류가 발생했습니다.");
            throw new RuntimeException(e);
        }
    }

    public void disconnect(Long userId) {
        emitterRepository.disconnect(userId);
    }

}
