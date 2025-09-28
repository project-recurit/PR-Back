package com.example.sideproject.domain.notification.publisher;

import com.example.sideproject.domain.applicant.entity.ApplicationStatus;
import com.example.sideproject.domain.notification.dto.EventDto;
import com.example.sideproject.domain.notification.dto.EventListDto;
import com.example.sideproject.fake.FakeApplicationEventPublisher;
import com.example.sideproject.global.enums.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.sideproject.domain.notification.entity.NotificationType.APPLICATION_RESULT;
import static com.example.sideproject.domain.notification.entity.NotificationType.PROJECT_APPLICANT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ApplicantNotificationTest {
    ApplicantNotification applicantNotification;
    FakeApplicationEventPublisher fakePublisher;

    @BeforeEach
    void setUp() {
        fakePublisher = new FakeApplicationEventPublisher();

        applicantNotification = new ApplicantNotification(fakePublisher);
    }

    @DisplayName("지원자 등록 객체를 정상적으로 생성")
    @Test
    void registerApplicant() {
        // given
        Long projectId = 1L;
        String projectTitle = "테스트 프로젝트";
        Position position = Position.FRONTEND;
        Long leaderId = 100L;
        boolean isPushAllowed = true;

        // when
        applicantNotification.registerApplicant(projectId, projectTitle,
                                                position, leaderId, isPushAllowed);

        // then
        assertThat(fakePublisher.getEventCount()).isEqualTo(1);

        EventListDto publishedEvent = fakePublisher.getLastEvent();

        assertThat(publishedEvent).isNotNull();

        EventDto eventDto = publishedEvent.eventDtos().get(0);

        assertThat(eventDto.to()).isEqualTo(leaderId);
        assertThat(eventDto.relatedId()).isEqualTo(projectId);
        assertThat(eventDto.msg()).isEqualTo(String.format(
                PROJECT_APPLICANT.getMessage(), projectTitle, position.name()));
    }

    @DisplayName("지원자 상태 변경 알림 객체를 정상적으로 생성")
    @Test
    void changeApplicantStatus() {
        // given
        Long projectId = 1L;
        String projectTitle = "테스트 프로젝트";
        Long userId = 99L;
        boolean isPushAllowed = true;
        ApplicationStatus status = ApplicationStatus.viewed;

        // when
        applicantNotification.changeApplicantStatus(projectId, projectTitle,
                                                    status, userId, isPushAllowed);

        // then
        EventListDto publishedEvent = fakePublisher.getLastEvent();

        assertThat(publishedEvent).isNotNull();

        EventDto eventDto = publishedEvent.eventDtos().get(0);

        assertThat(eventDto.to()).isEqualTo(userId);
        assertThat(eventDto.relatedId()).isEqualTo(projectId);
        assertThat(eventDto.msg()).isEqualTo(String.format(APPLICATION_RESULT.getMessage(), projectTitle, status.getDescription()));
        assertThat(eventDto.needToPush()).isEqualTo(status.isNotify());
    }
}