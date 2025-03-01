package com.example.sideproject.domain.favorite.service;

import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.project.repository.ProjectRepository;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FavoriteProjectService {
    private final ProjectRepository projectRepository;

    public void validateProject(Long projectId, User user) {
        // NOTE: 팀원 구인 글이 존재하는지 확인 -> 예외처리
        Project project = findProject(projectId);

        // NOTE: 자신이 작성한 글이 아닌지 확인 -> 예외처리
        if (Objects.equals(project.getUser().getId(), user.getId())) {
            throw new CustomException(ErrorType.NOT_MODIFY_OWN);
        }
    }

    private Project findProject(Long teamRecruitId) {
        return projectRepository.findById(teamRecruitId)
                .orElseThrow(() -> new CustomException(ErrorType.TEAM_RECRUIT_NOT_FOUND));
    }

}
