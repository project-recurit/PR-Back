package com.example.sideproject.domain.notification.publisher;

import com.example.sideproject.domain.notification.dto.EventDto;
import com.example.sideproject.domain.notification.dto.EventListDto;
import com.example.sideproject.fake.FakeApplicationEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.sideproject.domain.notification.entity.NotificationType.CHAT_START;
import static org.assertj.core.api.Assertions.assertThat;

class ChatNotificationTest {
    ChatNotification applicantNotification;
    FakeApplicationEventPublisher fakePublisher;

    @BeforeEach
    void setUp() {
        fakePublisher = new FakeApplicationEventPublisher();

        applicantNotification = new ChatNotification(fakePublisher);
    }

    @DisplayName("채팅 생성 알림 객체를 정상적으로 생성한다")
    @Test
    void createRoom() {
        // given
        String senderName = "유저1";
        Long relatedId = 1L;
        Long receiverId = 100L;
        boolean isPushAllowed = true;

        // when
        applicantNotification.createRoom(senderName, relatedId, receiverId, isPushAllowed);

        // then
        EventListDto publishedEvent = fakePublisher.getLastEvent();

        assertThat(publishedEvent).isNotNull();

        EventDto eventDto = publishedEvent.eventDtos().get(0);

        assertThat(eventDto.to()).isEqualTo(receiverId);
        assertThat(eventDto.relatedId()).isEqualTo(relatedId);
        assertThat(eventDto.msg()).isEqualTo(String.format(CHAT_START.getMessage(), senderName));
    }
}