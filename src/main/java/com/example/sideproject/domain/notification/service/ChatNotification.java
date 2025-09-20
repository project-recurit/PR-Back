package com.example.sideproject.domain.notification.service;

import com.example.sideproject.domain.notification.dto.EventDto;
import com.example.sideproject.domain.notification.dto.EventListDto;
import com.example.sideproject.domain.notification.entity.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatNotification {
    private final ApplicationEventPublisher publisher;

    /**
     * 채팅방 생성 시 알림
     * @param senderName 생성한 유저명
     * @param relatedId 연관된 pk
     * @param receiverId 알림 받을 유저
     */
    public void createRoom(String senderName, Long relatedId,
                           Long receiverId, boolean isPushAllowed) {

        NotificationType notificationType = NotificationType.CHAT_START;

        String msg = String.format(notificationType.getMessage(), senderName);

        EventDto eventDto = EventDto.builder()
                .to(receiverId)
                .msg(msg)
                .relatedId(relatedId)
                .type(notificationType)
                .needToPush(notificationType.isNeedToPush())
                .pushAllowed(isPushAllowed)
                .build();

        EventListDto eventListDto = new EventListDto(List.of(eventDto));
        publisher.publishEvent(eventListDto);
    }
}
