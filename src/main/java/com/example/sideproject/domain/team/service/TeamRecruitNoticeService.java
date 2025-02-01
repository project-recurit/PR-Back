package com.example.sideproject.domain.team.service;

import com.example.sideproject.domain.team.entity.TeamRecruit;
import com.example.sideproject.domain.user.entity.TechStack1;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.notification.aop.annotation.NotifyOn;
import com.example.sideproject.global.notification.dto.EventDto;
import com.example.sideproject.global.notification.dto.EventListDto;
import com.example.sideproject.global.notification.entity.NotificationType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeamRecruitNoticeService {

    @NotifyOn
    public EventListDto notice(TeamRecruit recruit, List<User> users, Set<TechStack1> techStack1s) {
        String msg = "\'" + recruit.getTitle() + "\' 가 등록되었습니다.";
        return new EventListDto(users.stream()
                .map(user -> new EventDto(
                        user.getId(),
                        recruit.getUser().getId(),
                        findFitTechStacks(user.getTechStack1s(), techStack1s) + " $| " + msg,
                        NotificationType.PROJECT_REGISTRATION,
                        "/api/v1/team-recruit/" + recruit.getId())
                ).toList()
        );
    }


    /**
     *
     * @param reqStacks
     * @param myStacks
     * @return
     */
    private String findFitTechStacks(Set<TechStack1> reqStacks, Set<TechStack1> myStacks) {
        return reqStacks.stream()
                .filter(myStacks::contains)
                .map(TechStack1::getDisplayName)
                .collect(Collectors.joining(","));
    }
}
