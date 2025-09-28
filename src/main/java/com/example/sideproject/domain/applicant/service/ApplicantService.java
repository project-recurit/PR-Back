package com.example.sideproject.domain.applicant.service;

import com.example.sideproject.domain.applicant.dto.ApplicantApplyDto;
import com.example.sideproject.domain.applicant.dto.ApplicantResponseDto;
import com.example.sideproject.domain.applicant.dto.search.SearchApplicantDto;
import com.example.sideproject.domain.applicant.entity.Applicant;
import com.example.sideproject.domain.applicant.entity.ApplicationStatus;
import com.example.sideproject.domain.applicant.repository.ApplicantRepository;
import com.example.sideproject.domain.applicant.repository.query.ApplicantQueryRepository;
import com.example.sideproject.domain.notification.publisher.ApplicantNotification;
import com.example.sideproject.domain.recruitment.entity.Recruitment;
import com.example.sideproject.domain.recruitment.service.RecruitmentService;
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
    private final RecruitmentService recruitmentService;
    private final ApplicantQueryRepository applicantQueryRepository;
    private final ApplicantNotification applicantNotification;

    /**
     * 프로젝트 지원
     */
    public Long apply(User user, Long recruitmentId, ApplicantApplyDto req) {
        // 프로젝트가 있는지 확인
        Recruitment recruitment = recruitmentService.findRecruitment(recruitmentId);

        // 지원 내역 확인
        if (applicantRepository.existsByRecruitmentAndUser(recruitment, user)) {
            throw new CustomException(ErrorType.DUPLICATE_APPLICATION);
        }

        Applicant applicant = Applicant.builder()
                .recruitment(recruitment)
                .user(user)
                .position(req.position())
                .status(ApplicationStatus.unviewed)
                .build();

        applicantNotification.registerApplicant(
                recruitmentId,
                recruitment.getTitle(),
                applicant.getPosition(),
                recruitment.getUser().getId(),
                recruitment.getUser().isPushAllowed()
        );

        return applicantRepository.save(applicant).getId();
    }

    /**
     * 프로젝트 지원 상태 변경
     */
    @Transactional
    public void updateStatus(User user, Long recruitmentId, Long applicantId, ApplicationStatus status) {
        Recruitment recruitment = recruitmentService.findRecruitment(recruitmentId);
        if (!recruitment.isRecruitmentLeader(user.getId())) {
            throw new CustomException(ErrorType.APPLICANT_NOT_FOUND);
        }

        Applicant applicant = applicantRepository.findByIdAndRecruitment(applicantId, recruitment)
                .orElseThrow(() -> new CustomException(ErrorType.APPLICANT_NOT_FOUND));

        applicant.updateStatus(status);

        if (status.isNotify()) {
            applicantNotification.changeApplicantStatus(
                    recruitmentId,
                    recruitment.getTitle(),
                    status,
                    applicant.getUser().getId(),
                    applicant.getUser().isPushAllowed()
            );
        }
    }

    /**
     * 프로젝트 지원 삭제
     */
    public void cancel(User user, Long recruitmentId, Long applicantId) {
        Recruitment recruitment = recruitmentService.findRecruitment(recruitmentId);
        Applicant applicant = applicantRepository.findByIdAndRecruitment(applicantId, recruitment)
                .orElseThrow(() -> new CustomException(ErrorType.APPLICANT_NOT_FOUND));

        if (!applicant.isOwn(user.getId())) {
            throw new CustomException(ErrorType.APPLICANT_NOT_FOUND);
        }

        applicantRepository.delete(applicant);
    }

    /**
     * 해당 프로젝트의 지원자 목록 조회
     */
    public List<ApplicantResponseDto> getApplicants(User user, Long recruitmentId, SearchApplicantDto searchDto) {
        return applicantQueryRepository.findApplicants(user.getId(), recruitmentId, searchDto);
    }
}
