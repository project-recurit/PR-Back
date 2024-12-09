package com.example.sideproject.global.notification.service;

import com.example.sideproject.global.notification.dto.EventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final SseService sseService;

    @EventListener
    public void send(EventDto eventDto) {
        sseService.send(eventDto);
    }
}
