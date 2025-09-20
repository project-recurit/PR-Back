package com.example.sideproject.domain.notification.service;

import com.example.sideproject.domain.notification.dto.recruitment.RecruitmentNotificationDto;
import com.example.sideproject.domain.recruitment.entity.Recruitment;
import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.entity.UserTechStack;
import com.example.sideproject.domain.notification.dto.EventDto;
import com.example.sideproject.domain.notification.dto.EventListDto;
import com.example.sideproject.domain.notification.entity.NotificationType;
import com.example.sideproject.global.component.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruitmentNotification {
    private final ApplicationEventPublisher publisher;
    private final Converter converter;

    /**
     * 해당하는 유저에게 알림 메시지를 전달한다.
     * 알림 스프링 이벤트 연동
     * @param recruitment 등록한 프로젝트
     * @param users 유저 리스트
     * @param recruitmentTechStackIds 등록한 프로젝트의 기술 스택 아이디 리스트
     * @return 이벤트 객체
     */
    public void notice(Recruitment recruitment, List<User> users, List<Long> recruitmentTechStackIds) {

        NotificationType notificationType = NotificationType.PROJECT_REGISTRATION;

        // 알림 발송을 위해 유저의 기술 스택에 맞는 메시지 생성
        List<EventDto> eventDtos = getEventDtos(recruitment, users, recruitmentTechStackIds, notificationType);

        publisher.publishEvent(new EventListDto(eventDtos));
    }

    /**
     * 푸시 알림 발송을 위해 이벤트 메시지 객체를 생성한다.
     * @param recruitment 프로젝트
     * @param users 유저 목록
     * @param recruitmentTechStackIds 프로젝트의 기술 스택 아이디
     * @param notificationType 알림 타입
     * @return 이벤트 리스트
     */
    private List<EventDto> getEventDtos(Recruitment recruitment, List<User> users,
                                        List<Long> recruitmentTechStackIds, NotificationType notificationType) {
        return users.stream()
                .map(user -> getEventDto(recruitment, recruitmentTechStackIds, user, notificationType))
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * 유저의 기술 스택에 맞는 메시지를 생성하고 그에 맞는 이벤트 객체를 생성한다.
     * @param recruitment 프로젝트
     * @param recruitmentTechStackIds 프로젝트의 기술 스택 아이디
     * @param user 유저 목록
     * @param notificationType 알림 타입
     * @return 이벤트 객체
     */
    private EventDto getEventDto(Recruitment recruitment, List<Long> recruitmentTechStackIds,
                                 User user, NotificationType notificationType) {

        // 등록한 게시글의 기술 스택에 해당하는 유저의 기술 스택만 가져온다.
        List<String> matchedTechStacks = matchTechStacks(getTechStackList(user), recruitmentTechStackIds);

        // 해당하는 기술 스택이 없는 경우 필터링
        if (matchedTechStacks.isEmpty()) {
            return null;
        }

        // 등록 메시지
        String title = String.format(notificationType.getMessage(), recruitment.getTitle());

        RecruitmentNotificationDto notificationDto = new RecruitmentNotificationDto(matchedTechStacks, title);

        return EventDto.builder()
                .to(user.getId())
                .from(recruitment.getUser().getId())
                .msg(converter.toString(notificationDto))
                .type(notificationType)
                .relatedId(recruitment.getId())
                .needToPush(notificationType.isNeedToPush())
                .pushAllowed(user.isPushAllowed())
                .build();
    }

    /**
     * 유저의 기술 스택 리스트를 추출하여 가져온다.
     * @param user 알림 받을 유저
     * @return 기술 스택 리스트
     */
    private List<TechStack> getTechStackList(User user) {
        return user.getUserTechStacks().stream()
                .map(UserTechStack::getTechStack)
                .toList();
    }

    /**
     * 알림 받을 유저와 매칭되는 기술 스택만 가져온다.
     * @param reqStacks 알림 받을 유저의 기술 스택
     * @param recruitmentTechStackIds 등록한 프로젝트의 기술 스택 아이디 리스트
     * @return 등록한 프로젝트 기술 스택과 일치하는 스택만 반환
     */
    private List<String> matchTechStacks(List<TechStack> reqStacks, List<Long> recruitmentTechStackIds) {
        return reqStacks.stream()
                .filter(techStack -> recruitmentTechStackIds.contains(techStack.getId()))
                .map(TechStack::getName)
                .filter(name -> !name.isEmpty())
                .toList();
    }

}
