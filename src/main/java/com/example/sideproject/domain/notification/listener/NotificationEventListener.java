package com.example.sideproject.domain.notification.listener;

import com.example.sideproject.domain.notification.dto.EventDto;
import com.example.sideproject.domain.notification.dto.EventListDto;
import com.example.sideproject.domain.notification.dto.NotificationRequestDto;
import com.example.sideproject.domain.notification.service.NotificationService;
import com.example.sideproject.domain.notification.service.SseService;
import com.example.sideproject.mq.NotificationMessage;
import com.example.sideproject.mq.FcmNotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationEventListener {
    private final SseService sseService;
    private final NotificationService notificationService;
    private final FcmNotificationSender fcmNotificationSender;

    /**
     * 알림 데이터 전송
     * @param listDto 알림 리스트
     */
    @Async("taskExecutor")
    @EventListener
    public void send(EventListDto listDto) {
        listDto.eventDtos().forEach(this::sendNotification);
    }

    private void sendNotification(EventDto eventDto) {
        // sse 전송
        sseService.send(eventDto);

        // 알림 서버로 전송
        if (eventDto.needToPush() && eventDto.pushAllowed()) {
            NotificationMessage sendMessage = new NotificationMessage(eventDto.to(), eventDto.title(), eventDto.msg());
            fcmNotificationSender.send(sendMessage);
        }

        // 알림 데이터 저장
        NotificationRequestDto requestDto = NotificationRequestDto.of(eventDto);
        notificationService.createNotification(requestDto);
    }

}
