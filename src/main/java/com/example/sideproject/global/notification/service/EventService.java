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
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final SseService sseService;
    private final NotificationService notificationService;

    /**
     * sse로 알림 데이터 전송
     * @param listDto 알림 리스트
     */
    @EventListener
    public void send(EventListDto listDto) {
        filterEvents(listDto).forEach(sseService::send);
    }

    /**
     * 알림 데이터를 데이터베이스에 저장한다.
     * @param listDto 알림 리스트
     */
    @Async("taskExecutor")
    @EventListener
    public void save(EventListDto listDto) {
        filterEvents(listDto).forEach(eventDto -> {
            NotificationRequestDto requestDto = NotificationRequestDto.of(eventDto);
            notificationService.createNotification(requestDto, new User(eventDto.from()));
        });
    }

    /**
     * 본인에게는 알림이 안가도록 필터링
      * @param listDto 알림 리스트
     * @return 본인 제외한 알림 리스트
     */
    private List<EventDto> filterEvents(EventListDto listDto) {
        return listDto.eventDtos().stream()
                .filter(event -> !Objects.equals(event.from(), event.to()))
                .toList();
    }
}
