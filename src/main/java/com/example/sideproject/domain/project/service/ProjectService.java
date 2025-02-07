package com.example.sideproject.domain.project.service;

import com.example.sideproject.domain.project.dto.CreateTeamRecruitPageResponseDto;
import com.example.sideproject.domain.project.dto.CreateTeamRecruitRequestDto;
import com.example.sideproject.domain.project.dto.CreateTeamRecruitResponseDto;
import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.project.entity.ProjectTechStack;
import com.example.sideproject.domain.project.repository.ProjectRepository;
import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.techstack.repository.TechStackRepository;
import com.example.sideproject.domain.user.entity.TechStack1;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.entity.UserStatus;
import com.example.sideproject.domain.user.repository.UserRepository;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


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

    /**
     * 프로젝트 구인 글 생성
     */
    @Transactional
    public void createTeamRecruit(CreateTeamRecruitRequestDto requestDto, User user) throws IOException {

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
        if(!requestDto.files().isEmpty()) {
            for (MultipartFile url : requestDto.files()) {
                projectUrlService.createProjectUrl(project, url);
            }
        }
//        List<User> users = findUserByTechStacks(project.getTechStack1s());
//        projectNoticeService.notice(project, users, techStackIds);
    }

    private List<User> findUserByTechStacks(Set<TechStack1> techStack1s) {
        return userRepository.findByTechStack1sIn(techStack1s);
    }


    public CreateTeamRecruitResponseDto getTeamRecruit(Long teamRecruitId) {
        Project project = findProject(teamRecruitId);
        return new CreateTeamRecruitResponseDto(project);
    }

    @Transactional
    public void updateTeamRecruit(Long teamRecruitId, CreateTeamRecruitRequestDto requestDto, User user) {

        User foundUser = validateActiveUser(user);
        Project project = findProject(teamRecruitId);
        validateTeamRecruitOwner(project, foundUser);

        if (foundUser.getNickname().equals(project.getUser().getNickname())) {
            requestDto.toEntity(user);
        }
    }

    @Transactional(readOnly = true)
    public CreateTeamRecruitPageResponseDto getTeamRecruits(String page) {

        // 기본값: 페이지 0, 사이즈 10
        int pageNumber = (page != null) ? Integer.parseInt(page) : 0;
        int pageSize = 10;

        // 기본 정렬: 생성일 기준 내림차순
        Sort sorting = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sorting);

        Page<Project> teamRecruitPage = projectRepository.findAll(pageable);

        List<CreateTeamRecruitResponseDto> teamRecruits = teamRecruitPage.getContent()
                .stream()
                .map(CreateTeamRecruitResponseDto::new)
                .collect(Collectors.toList());

        return new CreateTeamRecruitPageResponseDto(
                teamRecruits,
                teamRecruitPage.getNumber(),
                teamRecruitPage.getTotalPages(),
                teamRecruitPage.getTotalElements()
        );
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

    public Project findProject(Long teamRecruitId) {
        return projectRepository.findById(teamRecruitId)
                .orElseThrow(() -> new CustomException(ErrorType.TEAM_RECRUIT_NOT_FOUND));
    }

    private void validateTeamRecruitOwner(Project project, User user) {
        if (!Objects.equals(project.getUser().getId(), user.getId())) {
            throw new CustomException(ErrorType.NOT_YOUR_POST);
        }
    }

}


