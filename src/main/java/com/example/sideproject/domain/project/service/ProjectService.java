package com.example.sideproject.domain.project.service;

import com.example.sideproject.domain.project.dto.CreateTeamRecruitRequestDto;
import com.example.sideproject.domain.project.dto.CreateTeamRecruitResponseDto;
import com.example.sideproject.domain.project.dto.CreateTeamRecruitPageResponseDto;
import com.example.sideproject.domain.techstack.entity.TechStack;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.user.repository.UserRepository;
import com.example.sideproject.domain.user.entity.*;
import com.example.sideproject.global.exception.CustomException;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.domain.project.repository.ProjectRepository;

import java.util.Objects;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectNoticeService projectNoticeService;
    private final ProjectTechStackService projectTechStackService;
    private final ProjectUrlService projectUrlService;

    /**
     * 프로젝트 구인 글 생성
     */
    @Transactional
    public void createTeamRecruit(CreateTeamRecruitRequestDto requestDto, User user) {

        final User foundUser = validateActiveUser(user);
        final Project project = requestDto.toEntity(foundUser);
        projectRepository.save(project);

//        if (!techStacks.isEmpty()) {
//            for (TechStack tech : techStacks) { // 테크스택 유효성 검사 필요
//                projectTechStackService.createProjectTechStack(tech, project);
//            }
//        }
//        if(!projectUrls.isEmpty()) {
//            for (MultipartFile url : projectUrls) {
//                projectUrlService.createProjectUrl(project, url);
//            }
//        }

        // 기술스택에 해당하는 유저를 조회
        // List<User> users = findUserByTechStacks(project.getTechStacks());

        // 세번째 파라미터에 등록한 프로젝트의 기술 스택 ID 리스트를 넣는다.
        // projectNoticeService.notice(project, users, requestDto.getTechStackIds());
    }

    public List<User> findUserByTechStacks(List<TechStack> techStacks) {
        return userRepository.findByUserTechStacks_TechStackIn(techStacks);
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


