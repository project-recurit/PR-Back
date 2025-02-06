package com.example.sideproject.domain.project.service;

import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.user.entity.TechStack1;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.entity.UserTechStack;
import com.example.sideproject.global.notification.aop.annotation.NotifyOn;
import com.example.sideproject.global.notification.dto.EventDto;
import com.example.sideproject.global.notification.dto.EventListDto;
import com.example.sideproject.global.notification.entity.NotificationType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectNoticeService {

    @NotifyOn
    public EventListDto notice(Project project, List<User> to, List<TechStack> techStacks) {
        String msg = "\'" + project.getTitle() + "\' 가 등록되었습니다.";
        return new EventListDto(to.stream()
                .map(user -> new EventDto(
                        user.getId(),
                        project.getUser().getId(),
                        getMatchingTechStacks(user.getUserTechStacks().stream()
                                        .map(UserTechStack::getTechStack).toList(),
                                techStacks) + " $| " + msg,
                        NotificationType.PROJECT_REGISTRATION,
                        "/api/v1/project/" + project.getId())
                ).toList()
        );
    }


    /**
     *
     * @param reqStacks 알림 받을 유저의 기술 스택
     * @param myStacks 등록한 프로젝트의 기술 스택
     * @return 등록한 프로젝트 기술 스택과 일치하는 스택만 반환
     */
    private String getMatchingTechStacks(List<TechStack> reqStacks, List<TechStack> myStacks) {
        return reqStacks.stream()
                .filter(myStacks::contains)
                .map(TechStack::getName)
                .filter(name -> !name.isEmpty())
                .collect(Collectors.joining(","));
    }
}
