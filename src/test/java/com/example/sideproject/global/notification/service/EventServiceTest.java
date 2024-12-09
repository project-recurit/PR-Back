package com.example.sideproject.global.notification.service;

import com.example.sideproject.global.notification.aop.annotation.NotifyOn;
import com.example.sideproject.global.notification.dto.EventDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

@SpringBootTest
class EventServiceTest {

    @Autowired
    SseService sseService;

    @Autowired
    EventPushTest eventPushTest;

    @Test
    void eventTest() {
        Long userId = 1L;
        Long userId2 = 2L;
        sseService.connect(userId);
        sseService.connect(userId2);
        eventPushTest.subscribe();
        sseService.disconnect(userId);
        sseService.disconnect(userId2);
    }
}

@Component
class EventPushTest {
    @NotifyOn
    public EventDto subscribe() {
        return EventDto.builder()
                .to(1L)
                .msg("1에게 메시지 전달")
                .type("test")
                .build();
    }
}