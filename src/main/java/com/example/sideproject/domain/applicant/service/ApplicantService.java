package com.example.sideproject.domain.applicant.service;

import com.example.sideproject.domain.applicant.dto.ApplicantResponseDto;
import com.example.sideproject.domain.applicant.dto.search.SearchApplicantDto;
import com.example.sideproject.domain.applicant.entity.Applicant;
import com.example.sideproject.domain.applicant.entity.ApplicationStatus;
import com.example.sideproject.domain.applicant.repository.ApplicantRepository;
import com.example.sideproject.domain.applicant.repository.query.ApplicantQueryRepository;
import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.project.service.ProjectService;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    private final ProjectService projectService;
    private final ApplicantQueryRepository applicantQueryRepository;

    /**
     * 프로젝트 지원
     */
    public Long apply(User user, Long projectId) {
        // 프로젝트가 있는지 확인

        Project project = projectService.findProject(projectId);

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
//    @Transactional
//    public void updateStatus(User user, Long projectId, Long applicantId, ApplicationStatus status) {
//        Project project = Project.builder().id(projectId).build();
//        Applicant applicant = applicantRepository.findByIdAndProjectAndUser(applicantId, project, user)
//                .orElseThrow(() -> new CustomException(ErrorType.APPLICANT_NOT_FOUND));
//        applicant.updateStatus(status);
//    }

    /**
     * 프로젝트 지원 삭제
     */
    public void cancel(User user, Long projectId, Long applicantId) {
        Project project = projectService.findProject(projectId);
        Applicant applicant = applicantRepository.findByIdAndProjectAndUser(applicantId, project, user)
                .orElseThrow(() -> new CustomException(ErrorType.APPLICANT_NOT_FOUND));
        applicantRepository.delete(applicant);
    }

    /**
     * 해당 프로젝트의 지원자 목록 조회
     */
    public List<ApplicantResponseDto> getApplicants(User user, Long projectId, SearchApplicantDto searchDto) {
        return applicantQueryRepository.findApplicants(user.getId(), projectId, searchDto);
    }
}
