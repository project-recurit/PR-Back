package com.example.sideproject.domain.team.service;

import com.example.sideproject.domain.team.dto.CreateTeamRecruitRequestDto;
import com.example.sideproject.domain.team.dto.CreateTeamRecruitResponseDto;
import com.example.sideproject.domain.team.dto.CreateTeamRecruitPageResponseDto;
import com.example.sideproject.global.notification.aop.annotation.NotifyOn;
import com.example.sideproject.global.notification.dto.EventDto;
import com.example.sideproject.global.notification.entity.NotificationType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import com.example.sideproject.domain.team.entity.TeamRecruit;
import com.example.sideproject.domain.user.repository.UserRepository;
import com.example.sideproject.domain.user.entity.*;
import com.example.sideproject.global.exception.CustomException;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.domain.team.repository.TeamRecruitRepository;
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
public class TeamRecruitService {
    private final UserRepository userRepository;
    private final TeamRecruitRepository teamRecruitRepository;
    private final TeamRecruitNoticeService teamRecruitNoticeService;

    @Transactional
    public void createTeamRecruit(CreateTeamRecruitRequestDto requestDto, User user) {
        User foundUser = validateActiveUser(user);

        TeamRecruit teamRecruit = new TeamRecruit(
                null,
            requestDto.getTitle(),
            requestDto.getContent(),
            requestDto.getTechStacks(),
            requestDto.getExpectedPeriod(),
            requestDto.getFileUrl(),
            requestDto.getContact(),
            foundUser
        );

        teamRecruitRepository.save(teamRecruit);

        List<User> users = findUserByTechStacks(teamRecruit.getTechStacks());
        teamRecruitNoticeService.notice(teamRecruit, users, requestDto.getTechStacks());
    }

    private List<User> findUserByTechStacks(Set<TechStack> techStacks) {
        return userRepository.findByTechStacksIn(techStacks);
    }


    public CreateTeamRecruitResponseDto getTeamRecruit(Long teamRecruitId) {
        TeamRecruit teamRecruit = findTeamRecruit(teamRecruitId);
        return new CreateTeamRecruitResponseDto(teamRecruit);
    }

    @Transactional
    public void updateTeamRecruit(Long teamRecruitId, CreateTeamRecruitRequestDto requestDto, User user) {
        User foundUser = validateActiveUser(user);
        TeamRecruit teamRecruit = findTeamRecruit(teamRecruitId);
        validateTeamRecruitOwner(teamRecruit, foundUser);

        teamRecruit.update(
            requestDto.getTitle(),
            requestDto.getContent(),
            requestDto.getTechStacks(),
            requestDto.getExpectedPeriod(),
            requestDto.getFileUrl(),
            requestDto.getContact()
        );
    }

    @Transactional(readOnly = true)
    public CreateTeamRecruitPageResponseDto getTeamRecruits(String page) {                                                                                                                                                 
        
        // 기본값: 페이지 0, 사이즈 10
        int pageNumber = (page != null) ? Integer.parseInt(page) : 0;
        int pageSize = 10;
        
        // 기본 정렬: 생성일 기준 내림차순
        Sort sorting = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sorting);
        
        Page<TeamRecruit> teamRecruitPage = teamRecruitRepository.findAll(pageable);
        
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
        TeamRecruit teamRecruit = findTeamRecruit(teamRecruitId);
        validateTeamRecruitOwner(teamRecruit, foundUser);
        
        teamRecruitRepository.delete(teamRecruit);
    }

    
    private User validateActiveUser(User user) {
        User foundUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));
        
        if (foundUser.getUserStatus() != UserStatus.ACTIVE_USER) {
            throw new CustomException(ErrorType.WITHDRAW_USER);
        }
        
        return foundUser;
    }

    private TeamRecruit findTeamRecruit(Long teamRecruitId) {
        return teamRecruitRepository.findById(teamRecruitId)
                .orElseThrow(() -> new CustomException(ErrorType.TEAM_RECRUIT_NOT_FOUND));
    }
    private void validateTeamRecruitOwner(TeamRecruit teamRecruit, User user) {
        if (!Objects.equals(teamRecruit.getUser().getId(), user.getId())) {
            throw new CustomException(ErrorType.NOT_YOUR_POST);
        }
    }
}


