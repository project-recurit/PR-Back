package com.example.sideproject.domain.team.service;

import com.example.sideproject.domain.team.dto.CreateTeamRecruitRequestDto;
import com.example.sideproject.domain.team.dto.CreateTeamRecruitResponseDto;
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



@Service
@RequiredArgsConstructor
@Transactional
public class TeamRecruitService {
    private final UserRepository userRepository;
    private final TeamRecruitRepository teamRecruitRepository;

    @Transactional
    public void createTeamRecruit(CreateTeamRecruitRequestDto requestDto, User user) {
        User foundUser = validateActiveUser(user);

        TeamRecruit teamRecruit = new TeamRecruit(
            requestDto.getTitle(),
            requestDto.getContent(),
            requestDto.getTechStacks(),
            requestDto.getExpectedPeriod(),
            requestDto.getFileUrl(),
            requestDto.getContact(),
            foundUser
        );

        teamRecruitRepository.save(teamRecruit);
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


