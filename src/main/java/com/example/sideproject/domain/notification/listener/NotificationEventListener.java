package com.example.sideproject.domain.notification.listener;

import com.example.sideproject.domain.notification.dto.EventDto;
import com.example.sideproject.domain.notification.dto.EventListDto;
import com.example.sideproject.domain.notification.dto.NotificationRequestDto;
import com.example.sideproject.domain.notification.service.NotificationService;
import com.example.sideproject.domain.notification.service.SseService;
import com.example.sideproject.domain.notification.fcm.dto.payload.NotificationMessage;
import com.example.sideproject.domain.notification.fcm.service.FcmNotificationSender;
import com.example.sideproject.global.component.ObjectConverter;
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
public class NotificationEventListener {
    private final SseService sseService;
    private final NotificationService notificationService;
    private final FcmNotificationSender fcmNotificationSender;
    private final ObjectConverter objectConverter;

    /**
     * 알림 데이터 전송
     * @param listDto 알림 리스트
     */
    @EventListener
    public void send(EventListDto listDto) {
        // 본인 제외
        EventListDto filtered = filterEvents(listDto);

        // 알림 전송
        filtered.eventDtos().forEach(this::sendNotification);
    }

    /**
     * 알림 서버로 알림 전송 (비동기)
     * sse와 push 알림을 전송한다.
     * @param eventDto 알림 데이터
     */
    // TODO sse는 서버 스케일 아웃 시 정상적으로 작동하지 않음 => 메시지 브로커 필요
    @Async("taskExecutor")
    public void sendNotification(EventDto eventDto) {
        // sse 전송
        sseService.send(eventDto);

        // 푸시 허용한 사용자만 알림 서버로 전송
        if (eventDto.needToPush() && eventDto.pushAllowed()) {
            NotificationMessage sendMessage = new NotificationMessage(eventDto.to(), eventDto.title(), objectConverter.toString(eventDto.msg()));
            fcmNotificationSender.send(sendMessage);
        }

        // 알림 데이터 저장
        NotificationRequestDto requestDto = NotificationRequestDto.of(eventDto);
        notificationService.createNotification(requestDto);
    }

    /**
     * 본인에게는 알림이 안가도록 필터링
     * @param listDto 알림 리스트
     * @return 본인 제외한 알림 리스트
     */
    private EventListDto filterEvents(EventListDto listDto) {
        List<EventDto> filtered = listDto.eventDtos().stream()
                .filter(event -> !Objects.equals(event.from(), event.to()))
                .toList();
        return new EventListDto(filtered);
    }
}
