package com.example.sideproject.global.notification.service;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.notification.dto.EventDto;
import com.example.sideproject.global.notification.dto.NotificationRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final SseService sseService;
    private final NotificationService notificationService;

    @EventListener
    public void send(EventDto eventDto) {
        sseService.send(eventDto);
    }

    @Async("taskExecutor")
    @EventListener
    public void save(EventDto eventDto) {
        NotificationRequestDto requestDto = NotificationRequestDto.builder()
                .to(eventDto.to())
                .type(eventDto.type())
                .message(eventDto.msg())
                .relatedUrl(eventDto.relatedUrl())
                .build();

        notificationService.createNotification(requestDto, new User(eventDto.from()));
    }
}
