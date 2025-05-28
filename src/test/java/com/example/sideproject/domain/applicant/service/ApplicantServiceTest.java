package com.example.sideproject.domain.applicant.service;

import com.example.sideproject.domain.applicant.dto.ApplicantApplyDto;
import com.example.sideproject.domain.recruitment.dto.RecruitmentPositionRequestDto;
import com.example.sideproject.domain.recruitment.dto.RecruitmentRequestDto;
import com.example.sideproject.domain.recruitment.entity.EstimatedDuration;
import com.example.sideproject.domain.recruitment.entity.RecruitmentCategory;
import com.example.sideproject.domain.recruitment.service.RecruitmentService;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.Position;
import com.example.sideproject.global.enums.WorkType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicantServiceTest {
    @Autowired
    ApplicantService applicantService;

    @Autowired
    RecruitmentService recruitmentService;

    private Long recruitmentId;
    private User applicant = new User(2L);

    @Transactional
    @BeforeEach
    void setUp() {
        String title = "프로젝트 팀원 모집합니다.";
        String content = "팀원 모집";
        String deadLine = "2025-06-20";
        EstimatedDuration estimatedDuration = EstimatedDuration.WITHIN_THREE_MONTHS;
        WorkType workType = WorkType.ALL;
        RecruitmentCategory category = RecruitmentCategory.COMMUNITY;
        List<Long> techStackIds = List.of(105L, 99L, 108L);

        RecruitmentRequestDto req = new RecruitmentRequestDto(title, content, deadLine, estimatedDuration,
                                                              workType, category, false, techStackIds);

        User user = new User(1L);
        List<RecruitmentPositionRequestDto> positionsReq = List.of(
                new RecruitmentPositionRequestDto(2, Position.BACKEND),
                new RecruitmentPositionRequestDto(3, Position.FRONTEND)
        );

        recruitmentId = recruitmentService.createRecruitment(req, List.of(), positionsReq, user);
    }

    @DisplayName("프로젝트를 지원한다.")
    @Test
    @Transactional
    void apply() {
        ApplicantApplyDto applyDto = new ApplicantApplyDto(Position.BACKEND);
        applicantService.apply(applicant, recruitmentId, applyDto);
    }

    @Test
    void updateStatus() {
    }

    @Test
    void cancel() {
    }

    @Test
    void getApplicants() {
    }
}