package com.example.sideproject.domain.notification.service;

import com.example.sideproject.domain.recruitment.entity.Recruitment;
import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.entity.UserTechStack;
import com.example.sideproject.domain.notification.aop.annotation.NotifyOn;
import com.example.sideproject.domain.notification.dto.EventDto;
import com.example.sideproject.domain.notification.dto.EventListDto;
import com.example.sideproject.domain.notification.entity.NotificationType;
import com.example.sideproject.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecruitmentNotificationService {

    /**
     * 해당하는 유저에게 알림 메시지를 전달한다.
     * 알림 스프링 이벤트 연동
     * @param recruitment 등록한 프로젝트
     * @param users 유저 리스트
     * @param recruitmentTechStackIds 등록한 프로젝트의 기술 스택 아이디 리스트
     * @return 이벤트 객체
     */
    @NotifyOn
    public EventListDto notice(Recruitment recruitment, List<User> users, List<Long> recruitmentTechStackIds) {
        String msg = "\'" + recruitment.getTitle() + "\' 가 등록되었습니다.";
        return new EventListDto(users.stream()
                .map(user -> getEventDto(recruitment, recruitmentTechStackIds, user, msg)).toList()
        );
    }

    private EventDto getEventDto(Recruitment recruitment, List<Long> recruitmentTechStackIds, User user, String msg) {
        String resultMsg = getMatchingTechStacks(
                user.getUserTechStacks().stream().map(UserTechStack::getTechStack).toList(),
                recruitmentTechStackIds) + " $| " + msg;

        return EventDto.builder()
                .to(user.getId())
                .from(recruitment.getUser().getId())
                .msg(resultMsg)
                .type(NotificationType.PROJECT_REGISTRATION)
                .relatedId(recruitment.getId())
                .needToPush(true)
                .pushAllowed(user.isPushAllowed())
                .build();
    }

    /**
     *
     * @param reqStacks 알림 받을 유저의 기술 스택
     * @param recruitmentTechStackIds 등록한 프로젝트의 기술 스택 아이디 리스트
     * @return 등록한 프로젝트 기술 스택과 일치하는 스택만 반환
     */
    private String getMatchingTechStacks(List<TechStack> reqStacks, List<Long> recruitmentTechStackIds) {
        return reqStacks.stream()
                .filter(techStack -> recruitmentTechStackIds.contains(techStack.getId()))
                .map(TechStack::getName)
                .filter(name -> !name.isEmpty())
                .collect(Collectors.joining(","));
    }

}
