package com.example.sideproject.domain.personal.service;

import com.example.sideproject.domain.personal.dto.CreateProjectRecruitRequestDto;
import com.example.sideproject.domain.personal.dto.CreateProjectRecruitResponseDto;
import com.example.sideproject.domain.personal.entity.ProjectRecruit;
import com.example.sideproject.domain.personal.repository.ProjectRecruitRepository;
import com.example.sideproject.domain.user.repository.UserRepository;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.entity.UserStatus;
import com.example.sideproject.global.exception.CustomException;
import com.example.sideproject.global.enums.ErrorType;  
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Transactional
public class ProjectRecruitService {
    private final UserRepository userRepository;
    private final ProjectRecruitRepository projectRecruitRepository;

    @Transactional
    public void createProjectRecruit(CreateProjectRecruitRequestDto requestDto, User user) {
        User foundUser = validateActiveUser(user);

        ProjectRecruit projectRecruit = new ProjectRecruit(
            requestDto.getTitle(),
            requestDto.getContent(),
            requestDto.getTechStacks(),
            requestDto.getExpectedPeriod(),
            requestDto.getFileUrl(),
            requestDto.getContact(),
            foundUser
        );
        
        projectRecruitRepository.save(projectRecruit);
    }

    public CreateProjectRecruitResponseDto getProjectRecruit(Long projectRecruitId) {
        ProjectRecruit projectRecruit = findProjectRecruit(projectRecruitId);
        return new CreateProjectRecruitResponseDto(projectRecruit);
    }

    @Transactional
    public void updateProjectRecruit(Long projectRecruitId, CreateProjectRecruitRequestDto requestDto, User user) {
        User foundUser = validateActiveUser(user);
        ProjectRecruit projectRecruit = findProjectRecruit(projectRecruitId);
        validateProjectRecruitOwner(projectRecruit, foundUser);

        projectRecruit.update(
            requestDto.getTitle(),
            requestDto.getContent(),
            requestDto.getTechStacks(),
            requestDto.getExpectedPeriod(),
            requestDto.getFileUrl(),
            requestDto.getContact()
        );
    }

    @Transactional
    public void deleteProjectRecruit(Long projectRecruitId, User user) {
        User foundUser = validateActiveUser(user);
        ProjectRecruit projectRecruit = findProjectRecruit(projectRecruitId);
        validateProjectRecruitOwner(projectRecruit, foundUser);
        
        projectRecruitRepository.delete(projectRecruit);
    }

    private User validateActiveUser(User user) {
        User foundUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));
        
        if (foundUser.getUserStatus() != UserStatus.ACTIVE_USER) {
            throw new CustomException(ErrorType.WITHDRAW_USER);
        }
        
        return foundUser;
    }

    private ProjectRecruit findProjectRecruit(Long projectRecruitId) {
        return projectRecruitRepository.findById(projectRecruitId)
                .orElseThrow(() -> new CustomException(ErrorType.PROJECT_RECRUIT_NOT_FOUND));
    }

    private void validateProjectRecruitOwner(ProjectRecruit projectRecruit, User user) {
        if (!Objects.equals(projectRecruit.getUser().getId(), user.getId())) {       
            throw new CustomException(ErrorType.NOT_YOUR_POST);
        }
    }
} 