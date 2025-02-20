package com.example.sideproject.domain.project.service;

import com.example.sideproject.domain.project.dto.*;
import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.project.entity.ProjectTechStack;
import com.example.sideproject.domain.project.repository.ProjectRepository;
import com.example.sideproject.domain.project.repository.query.ProjectQueryRepository;
import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.techstack.repository.TechStackRepository;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.entity.UserStatus;
import com.example.sideproject.domain.user.repository.UserRepository;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectNoticeService projectNoticeService;
    private final ProjectTechStackService projectTechStackService;
    private final ProjectUrlService projectUrlService;
    private final TechStackRepository techStackRepository; // 임시
    private final ProjectQueryRepository projectQueryRepository;

    /**
     * 프로젝트 구인 글 생성
     * todo IOException 없에기
     */
    @Transactional
    public void createProject(ProjectRequestDto requestDto, User user) throws IOException {

        final User foundUser = validateActiveUser(user);
        final Project project = requestDto.toEntity(foundUser);
        projectRepository.save(project);

        if (!requestDto.projectTechStacks().isEmpty()) {

            // 테크스텍 있는거만 검증 한 후 List 반환
            // findAllById는 쿼리를 직접짠거랑 많이 다른게 없어서 적용
            List<TechStack> techStacks = techStackRepository.findAllById(requestDto.projectTechStacks());
            List<ProjectTechStack> projectTechStacks = new ArrayList<>();
            List<Long> techStackIds = new ArrayList<>();

            for (TechStack techStack : techStacks) {
                // 배열에 미리 넣어두기
                projectTechStacks.add(
                        ProjectTechStack.builder()
                                .techStack(techStack)
                                .project(project)
                                .build()
                );
                techStackIds.add(techStack.getId());
            }
            // 이 메서드 안에 saveAll
            projectTechStackService.createProjectTechStack(projectTechStacks);
        }
        if (!requestDto.files().isEmpty()) {
            for (MultipartFile url : requestDto.files()) {
                projectUrlService.createProjectUrl(project, url);
            }
        }

        // 기술스택에 해당하는 유저를 조회
        // List<User> users = findUserByTechStacks(project.getTechStacks());

        // 세번째 파라미터에 등록한 프로젝트의 기술 스택 ID 리스트를 넣는다.
        // projectNoticeService.notice(project, users, requestDto.getTechStackIds());
    }

    public List<User> findUserByTechStacks(List<TechStack> techStacks) {
        return userRepository.findByUserTechStacks_TechStackIn(techStacks);
    }

    /**
     * 게시글 상세 조회
     * 조회 시 viewCount + 1
     */
    public ProjectDetailResponseDto getProject(Long projectId) {

        return projectQueryRepository.getProject(projectId);
    }

    /**
     * 게시글 전체 조회
     */
    public Page<ProjectsResponseDto> getProjects(int page) {

        final Pageable pageable = PageRequest.of(page - 1, 20);

        return projectQueryRepository.getProjects(pageable);
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public void updateProject(Long projectId, ProjectUpdateDto requestDto, User user) throws IOException {

        User foundUser = validateActiveUser(user);
        Project project = findProject(projectId);
        validateTeamRecruitOwner(project, foundUser);

        List<Long> existUrls = projectUrlService.existUrls(projectId);
        System.out.println("********************기존 url******************************* \n" + existUrls);

        // 기존 URL 중 삭제할 것 찾기
        List<Long> urlsToDelete = findUrlsToDelete(existUrls, requestDto.existFiles());
        if (!urlsToDelete.isEmpty()) {
            projectUrlService.deleteUrls(urlsToDelete); // 삭제
        }

        // 새로운 파일이 존재하면 추가
        if (requestDto.newFiles() != null && !requestDto.newFiles().isEmpty()) {
            for (MultipartFile file : requestDto.newFiles()) {
                projectUrlService.createProjectUrl(project, file);
            }
        }

        Project updateProject = requestDto.update(user, projectId);
        project.updateTechStacks(requestDto.projectTechStacks());
        projectRepository.save(updateProject);
    }

    // 기존 URL에서 삭제해야 할 URL을 찾는 메서드
    private List<Long> findUrlsToDelete(List<Long> existUrls, List<Long> requestUrls) {
        for (Long requestUrl : requestUrls) {
            existUrls.remove(requestUrl); // 삭제 리스트에서 이미 요청한 URL 제거
        }
        return existUrls;
    }

    @Transactional
    public void deleteTeamRecruit(Long teamRecruitId, User user) {
        User foundUser = validateActiveUser(user);
        Project project = findProject(teamRecruitId);
        validateTeamRecruitOwner(project, foundUser);

        projectRepository.delete(project);
    }


    private User validateActiveUser(User user) {
        User foundUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));

        if (foundUser.getUserStatus() != UserStatus.ACTIVE_USER) {
            throw new CustomException(ErrorType.WITHDRAW_USER);
        }

        return foundUser;
    }

    public Project findProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(ErrorType.TEAM_RECRUIT_NOT_FOUND));
    }

    private void validateTeamRecruitOwner(Project project, User user) {
        if (!Objects.equals(project.getUser().getId(), user.getId())) {
            throw new CustomException(ErrorType.NOT_YOUR_POST);
        }
    }
}


