package com.example.sideproject.global.notification.service;

import com.example.sideproject.domain.notification.aop.annotation.NotifyOn;
import com.example.sideproject.domain.notification.dto.EventDto;
import com.example.sideproject.domain.notification.dto.EventListDto;
import com.example.sideproject.domain.notification.entity.NotificationType;
import com.example.sideproject.domain.notification.service.SseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.util.List;

@SpringBootTest
class NotificationEventListenerTest {

    @Autowired
    SseService sseService;

    @Autowired
    EventPushTest eventPushTest;

    @Test
    void eventTest() throws InterruptedException {
        Long userId = 1L;
        Long userId2 = 2L;
        sseService.connect(userId);
        sseService.connect(userId2);
        eventPushTest.subscribe();
        sseService.disconnect(userId);
        sseService.disconnect(userId2);

        Thread.sleep(1000);
    }

}

@Component
class EventPushTest {
    @NotifyOn
    public EventListDto subscribe() {
        return new EventListDto(List.of(
                EventDto.builder()
                        .to(1L)
                        .from(1L)
                        .msg("1에게 메시지 전달")
                        .type(NotificationType.PROJECT_REGISTRATION)
                        .build()
        ));
    }
}