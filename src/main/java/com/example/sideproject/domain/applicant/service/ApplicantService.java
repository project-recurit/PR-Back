package com.example.sideproject.domain.applicant.service;

import com.example.sideproject.domain.applicant.entity.Applicant;
import com.example.sideproject.domain.applicant.entity.ApplicationStatus;
import com.example.sideproject.domain.applicant.repository.ApplicantRepository;
import com.example.sideproject.domain.team.entity.TeamRecruit;
import com.example.sideproject.domain.team.service.TeamRecruitService;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    private final TeamRecruitService teamRecruitService;

    /**
     * 프로젝트 지원
     */
    public Long apply(User user, Long projectId) {
        // 프로젝트가 있는지 확인

        TeamRecruit project = TeamRecruit.builder().id(projectId).build();

        // 지원 내역 확인
        if (applicantRepository.existsByProjectAndUser(project, user)) {
            throw new CustomException(ErrorType.DUPLICATE_APPLICATION);
        }

        Applicant applicant = Applicant.builder()
                .project(project)
                .user(user)
                .status(ApplicationStatus.unviewed)
                .build();

        return applicantRepository.save(applicant).getId();
    }

    /**
     * 프로젝트 지원 상태 변경
     */
    @Transactional
    public void updateStatus(User user, Long projectId, Long applicantId, ApplicationStatus status) {
        TeamRecruit project = TeamRecruit.builder().id(projectId).build();
        Applicant applicant = applicantRepository.findByIdAndProjectAndUser(applicantId, project, user)
                .orElseThrow(() -> new CustomException(ErrorType.APPLICANT_NOT_FOUND));
        applicant.updateStatus(status);
    }

    /**
     * 프로젝트 지원 삭제
     */
    public void cancel(User user, Long projectId, Long applicantId) {
        TeamRecruit project = TeamRecruit.builder().id(projectId).build();
        Applicant applicant = applicantRepository.findByIdAndProjectAndUser(applicantId, project, user)
                .orElseThrow(() -> new CustomException(ErrorType.APPLICANT_NOT_FOUND));
        applicantRepository.delete(applicant);
    }
}
