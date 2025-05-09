package com.example.sideproject.domain.project.service;

import com.example.sideproject.domain.project.dto.ProjectRequestDto;
import com.example.sideproject.domain.project.dto.ProjectResponseDto;
import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.project.repository.ProjectRepository;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.service.UserService;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;

    /**
     * 프로젝트 생성
     */
    public void createProject(ProjectRequestDto requestDto, User user) {

        final User existUser = userService.findActiveUser(user.getId());
        final Project project = Project.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .teamCount(requestDto.getTeamCount())
                .user(existUser)
                .build();
        projectRepository.save(project);
    }

    /**
     * 프로젝트 조회
     */
    public ProjectResponseDto getProject(Long projectId) {

        final Project project = findProject(projectId);

        return ProjectResponseDto.builder()
                .title(project.getTitle())
                .description(project.getDescription())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .teamCount(project.getTeamCount())
                .userNickname(project.getUser().getNickname())
                .projectMemberList(project.getProjectMembers())
                .build();
    }

    public Project findProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(ErrorType.PROJECT_NOT_FOUND));
    }
}
