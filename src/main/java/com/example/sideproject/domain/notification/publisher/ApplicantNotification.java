package com.example.sideproject.domain.notification.publisher;

import com.example.sideproject.domain.applicant.entity.ApplicationStatus;
import com.example.sideproject.domain.notification.dto.EventDto;
import com.example.sideproject.domain.notification.dto.EventListDto;
import com.example.sideproject.domain.notification.entity.NotificationType;
import com.example.sideproject.global.enums.Position;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicantNotification {
    private final ApplicationEventPublisher publisher;

    /**
     * 지원자 등록 시 팀장에게 알림
     * @param projectId 프로젝트 고유번호
     * @param projectTitle 프로젝트명
     * @param position 지원한 직무
     * @param leaderId 팀장
     */
    public void registerApplicant(Long projectId, String projectTitle,
                                  Position position, Long leaderId,
                                  boolean isPushAllowed) {

        NotificationType notificationType = NotificationType.PROJECT_APPLICANT;

        String msg = String.format(notificationType.getMessage(), projectTitle, position.name());

        EventDto eventDto = EventDto.builder()
                .to(leaderId)
                .msg(msg)
                .relatedId(projectId)
                .type(notificationType)
                .needToPush(true)
                .pushAllowed(isPushAllowed)
                .build();

        EventListDto eventListDto = new EventListDto(List.of(eventDto));
        publisher.publishEvent(eventListDto);
    }

    /**
     * 지원서의 상태가 변경이 되면 해당 유저에게 알림
     * @param projectId 프로젝트 고유번호
     * @param projectTitle 프로젝트명
     * @param status 팀장이 변경한 지원서 상태
     * @param userId 지원자
     */
    public void changeApplicantStatus(Long projectId, String projectTitle,
                                              ApplicationStatus status, Long userId,
                                              boolean isPushAllowed) {

        NotificationType notificationType = NotificationType.APPLICATION_RESULT;

        String msg = String.format(notificationType.getMessage(), projectTitle, notificationType.getMessage());

        EventDto eventDto = EventDto.builder()
                .to(userId)
                .msg(msg)
                .relatedId(projectId)
                .type(notificationType)
                .needToPush(status.isNotify())
                .pushAllowed(isPushAllowed)
                .build();

        EventListDto eventListDto = new EventListDto(List.of(eventDto));
        publisher.publishEvent(eventListDto);
    }
}
