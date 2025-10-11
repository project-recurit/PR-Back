package com.example.sideproject.domain.applicant.repository.query;

import com.example.sideproject.domain.applicant.entity.Applicant;
import com.example.sideproject.domain.applicant.entity.ApplicationStatus;
import com.example.sideproject.domain.applicant.repository.ApplicantRepository;
import com.example.sideproject.domain.recruitment.entity.*;
import com.example.sideproject.domain.recruitment.repository.RecruitmentRepository;
import com.example.sideproject.domain.status.project.dto.StatusApplicantResponseDto;
import com.example.sideproject.domain.status.project.dto.StatusSearchRequest;
import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.techstack.repository.query.TechStackQueryRepository;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.entity.UserStatus;
import com.example.sideproject.domain.user.repository.UserRepository;
import com.example.sideproject.global.config.QuerydslConfig;
import com.example.sideproject.global.dto.DateSort;
import com.example.sideproject.global.dto.PageDto;
import com.example.sideproject.global.dto.SearchDto;
import com.example.sideproject.global.enums.Position;
import com.example.sideproject.global.enums.WorkType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ApplicantQueryRepository.class, QuerydslConfig.class, TechStackQueryRepository.class})
@Transactional
class ApplicantQueryRepositoryTest {
    @Autowired
    ApplicantQueryRepository applicantQueryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RecruitmentRepository recruitmentRepository;

    @Autowired
    ApplicantRepository applicantRepository;

    Applicant applicant;
    User user2;

    @BeforeEach
    void setUp() {
        TechStack techStack1 = new TechStack(1L, "Spring");
        TechStack techStack2 = new TechStack(2L, "Spring AI");

        User user = User.builder()
                .userId(1L)
                .nickname("user1")
                .email("user1@test.com")
                .socialId("1")
                .socialProvider("KAKAO")
                .username("test_user1")
                .userStatus(UserStatus.ACTIVE_USER)
                .build();

        user2 = User.builder()
                .userId(2L)
                .nickname("user2")
                .email("user2@test.com")
                .socialId("2")
                .socialProvider("KAKAO")
                .username("user2")
                .userStatus(UserStatus.ACTIVE_USER)
                .build();

        System.out.println("======== 유저 입력");

        user = userRepository.save(user);
        user2 = userRepository.save(user2);

        System.out.println("======== 유저 입력 완료");

        System.out.println("========모집글 작성 시작");

        Recruitment recruitment = Recruitment.builder()
                .id(1L)
                .user(user)
                .title("프로젝트 인원 모집")
                .content("프로젝트 인원을 모집합니다.")
                .estimatedDuration(EstimatedDuration.THREE_MONTHS)
                .deadLine("2025-10-10")
                .isCommercial(false)
                .isRecruiting(true)
                .workType(WorkType.ALL)
                .positions(List.of(
                        RecruitmentPosition.builder()
                                .capacity(1)
                                .position(Position.BACKEND)
                                .build(),
                        RecruitmentPosition.builder()
                                .capacity(1)
                                .position(Position.FRONTEND)
                                .build()))
                .recruitmentCategory(RecruitmentCategory.COMMUNITY)
                .build();

        // techStack 저장
        getRecruitmentTechStacks(techStack1, techStack2)
                        .forEach(recruitment::addRecruitmentTechStacks);

        ReflectionTestUtils.setField(recruitment, "modifiedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(recruitment, "createdAt", LocalDateTime.now());

        recruitment = recruitmentRepository.save(recruitment);


        System.out.println("========== 모집글 작성 완료");

        System.out.println("========== 지원서 작성 시작");

        applicant = Applicant.builder()
                .recruitment(recruitment)
                .position(Position.FRONTEND)
                .status(ApplicationStatus.unviewed)
                .user(user2)
                .build();

        Applicant applicant2 = Applicant.builder()
                .recruitment(recruitment)
                .position(Position.FRONTEND)
                .status(ApplicationStatus.viewed)
                .user(user2)
                .build();

        applicant = applicantRepository.save(applicant);
        applicant2 = applicantRepository.save(applicant2);
        System.out.println("========== 지원서 작성 완료");
    }

    List<RecruitmentTechStack> getRecruitmentTechStacks(TechStack techStack1, TechStack techStack2) {
        return List.of(RecruitmentTechStack.builder()
                               .techStack(techStack1)
                               .build(),
                       RecruitmentTechStack.builder()
                               .techStack(techStack2)
                               .build());
    }

    @DisplayName("내 지원 현황을 조회한다")
    @Test
    void findApplications() {
        StatusSearchRequest searchRequest = new StatusSearchRequest(
                SearchDto.create(),
                null
        );

        PageDto pageDto = searchRequest.searchDto().pageDto();
        PageRequest pageRequest = pageDto.toPageRequest();

        Page<StatusApplicantResponseDto> applications = applicantQueryRepository.findApplications(pageRequest, user2.getId(), searchRequest);
        assertThat(applications.getTotalElements()).isEqualTo(2);
    }

    @DisplayName("내 지원 현황을 필터링 하여 조회한다")
    @Test
    void searchApplications() {
        StatusSearchRequest searchRequest = new StatusSearchRequest(
                SearchDto.create(),
                ApplicationStatus.viewed
        );

        DateSort dateSort = searchRequest.searchDto().dateSort();
        PageDto pageDto = searchRequest.searchDto().pageDto();

        Sort sort = Sort.by(Sort.Order.desc(dateSort.getOrder()));
        PageRequest pageRequest = pageDto.toPageRequest(sort);

        Page<StatusApplicantResponseDto> applications = applicantQueryRepository.findApplications(pageRequest, user2.getId(), searchRequest);
        assertThat(applications.getTotalElements()).isEqualTo(1);
    }
}