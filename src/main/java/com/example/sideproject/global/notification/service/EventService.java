package com.example.sideproject.global.notification.service;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.notification.dto.EventDto;
import com.example.sideproject.global.notification.dto.EventListDto;
import com.example.sideproject.global.notification.dto.NotificationRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final SseService sseService;
    private final NotificationService notificationService;

    @EventListener
    public void send(EventListDto listDto) {
        listDto.eventDtos().forEach(sseService::send);
    }

    @Async("taskExecutor")
    @EventListener
    public void save(EventListDto listDto) {
        listDto.eventDtos().forEach(eventDto -> {
            NotificationRequestDto requestDto = NotificationRequestDto.of(eventDto);
            notificationService.createNotification(requestDto, new User(eventDto.from()));
        });
    }

}
