package com.example.sideproject.domain.applicant.service;

import com.example.sideproject.domain.applicant.dto.ApplicantApplyDto;
import com.example.sideproject.domain.applicant.entity.Applicant;
import com.example.sideproject.domain.applicant.repository.ApplicantRepository;
import com.example.sideproject.domain.applicant.repository.query.ApplicantQueryRepository;
import com.example.sideproject.domain.notification.publisher.ApplicantNotification;
import com.example.sideproject.domain.recruitment.dto.RecruitmentPositionRequestDto;
import com.example.sideproject.domain.recruitment.dto.RecruitmentRequestDto;
import com.example.sideproject.domain.recruitment.entity.EstimatedDuration;
import com.example.sideproject.domain.recruitment.entity.Recruitment;
import com.example.sideproject.domain.recruitment.entity.RecruitmentCategory;
import com.example.sideproject.domain.recruitment.repository.RecruitmentRepository;
import com.example.sideproject.domain.recruitment.service.RecruitmentService;
import com.example.sideproject.domain.resume.service.ResumeService;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.Position;
import com.example.sideproject.global.enums.WorkType;
import com.example.sideproject.global.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicantServiceTest {
    @InjectMocks
    ApplicantService applicantService;

    @Mock
    RecruitmentRepository recruitmentRepository;

    @Mock
    RecruitmentService recruitmentService;

    private Long recruitmentId;
    private final User applicant = new User(2L);

    @Mock
    private ApplicantRepository applicantRepository;

    @Mock
    ResumeService resumeService;

    @Mock
    ApplicantQueryRepository applicantQueryRepository;

    @MockBean
    ApplicantNotification applicantNotification;

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

//        recruitmentId = recruitmentService.createRecruitment(req, List.of(), positionsReq, user);
    }

    @DisplayName("프로젝트를 지원한다.")
    @Test
    @Transactional
    void apply() {
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

    @DisplayName("지원자와 모집글 작성자는 이력서 열람 가능")
    @Test
    void readResume() {
        User recruitmentOwner = new User(13L);

        Applicant applicant2 = Applicant.builder()
                .id(11L)
                .user(applicant)
                .recruitment(Recruitment.builder()
                                     .user(recruitmentOwner)
                                     .build())
                .build();

        when(applicantRepository.findById(any())).thenReturn(Optional.of(applicant2));

        // 지원자와 모집글 작성자는 이력서 열람 가능
        applicantService.readResume(recruitmentOwner, applicant2.getId());
        applicantService.readResume(applicant, applicant2.getId());

        // 다른 유저는 열람 불가
        assertThatThrownBy(() -> applicantService.readResume(new User(14L), applicant2.getId()))
                .isInstanceOf(CustomException.class);
    }
}