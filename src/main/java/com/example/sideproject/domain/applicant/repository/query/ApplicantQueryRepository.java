package com.example.sideproject.domain.applicant.repository.query;

import com.example.sideproject.domain.applicant.dto.ApplicantResponseDto;
import com.example.sideproject.domain.applicant.dto.search.SearchApplicantDto;
import com.example.sideproject.domain.applicant.entity.ApplicationStatus;
import com.example.sideproject.domain.applicant.entity.QApplicant;
import com.example.sideproject.domain.recruitment.dto.RecruitmentTechStackDto;
import com.example.sideproject.domain.recruitment.dto.RecruitmentsResponseDto;
import com.example.sideproject.domain.recruitment.entity.QRecruitment;
import com.example.sideproject.domain.recruitment.entity.QRecruitmentTechStack;
import com.example.sideproject.domain.status.project.dto.StatusApplicantResponseDto;
import com.example.sideproject.domain.techstack.dto.TechStackDto;
import com.example.sideproject.domain.techstack.repository.query.TechStackQueryRepository;
import com.example.sideproject.global.enums.Position;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ApplicantQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final TechStackQueryRepository techStackQueryRepository;

    private QApplicant qApplicant = QApplicant.applicant;
    private QRecruitment qRecruitment = QRecruitment.recruitment;
    private QRecruitmentTechStack qRecruitmentTechStack = QRecruitmentTechStack.recruitmentTechStack;

    public List<StatusApplicantResponseDto> findApplications(Long userId) {
        List<StatusApplicantResponseDto> applications = jpaQueryFactory.select(Projections.constructor(
                        StatusApplicantResponseDto.class,
                        Projections.constructor(
                                RecruitmentsResponseDto.class,
                                qRecruitment.id,
                                qRecruitment.title,
                                qRecruitment.user.nickname,
                                qRecruitment.viewCount,
                                qRecruitment.commentCount,
                                qRecruitment.modifiedAt,
                                qRecruitment.recruitmentCategory,
                                qRecruitment.isCommercial
                        ),
                        qApplicant.status
                )).from(qApplicant)
                .join(qApplicant.recruitment)
                .join(qApplicant.user)
                .where(
                        eqApplicationUserId(userId)
                ).fetch();


        List<Long> recruitmentIds = applications.stream()
                .map(it -> it.getRecruitment().getId())
                .toList();

        Map<Long, List<TechStackDto>> techStacks = techStackQueryRepository.getTechStacks(recruitmentIds, qRecruitmentTechStack);

        List<StatusApplicantResponseDto> result = applications.stream()
                .map(it -> it.setTechStacks(techStacks.get(it.getRecruitment().getId())))
                .toList();

        return result;
    }

    public List<ApplicantResponseDto> findApplicants(Long userId, Long recruitmentId, SearchApplicantDto searchDto) {
        List<ApplicantResponseDto> result = jpaQueryFactory.select(Projections.constructor(
                        ApplicantResponseDto.class,
                        qApplicant.id,
                        qApplicant.recruitment.id,
                        qApplicant.user.nickname,
                        qApplicant.position,
                        qApplicant.status,
                        qApplicant.createdAt,
                        qApplicant.modifiedAt
                )).from(qApplicant)
                .join(qApplicant.recruitment)
                .join(qApplicant.user)
                .where(
                        eqRecruitmentId(recruitmentId),
                        eqRecruitmentUserId(userId),
                        createSearchCondition(searchDto)
                ).fetch();

        return result;
    }

    private BooleanExpression createSearchCondition(SearchApplicantDto searchDto) {
        String nickname = searchDto.nickname();
        Position position = searchDto.position();
        ApplicationStatus status = searchDto.status();

        BooleanExpression result = Expressions.asString("1").eq("1");

        if (nickname != null) {
            result = result.and(qApplicant.user.nickname.like(nickname));
        }

        if (position != null) {
            result = result.and(qApplicant.position.eq(searchDto.position()));
        }

        if (status != null) {
            result = result.and(qApplicant.status.eq(status));
        }

        return result;
    }

    private BooleanExpression eqApplicationUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        return qApplicant.user.id.eq(userId);
    }

    private BooleanExpression eqRecruitmentUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        return qApplicant.recruitment.user.id.eq(userId);
    }

    private BooleanExpression eqRecruitmentId(Long recruitmentId) {
        if (recruitmentId == null) {
            return null;
        }
        return qApplicant.recruitment.id.eq(recruitmentId);
    }
}
