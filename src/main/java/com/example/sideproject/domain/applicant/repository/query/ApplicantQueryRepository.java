package com.example.sideproject.domain.applicant.repository.query;

import com.example.sideproject.domain.applicant.dto.ApplicantResponseDto;
import com.example.sideproject.domain.applicant.dto.search.SearchApplicantDto;
import com.example.sideproject.domain.applicant.entity.ApplicationStatus;
import com.example.sideproject.domain.applicant.entity.QApplicant;
import com.example.sideproject.domain.recruitment.dto.RecruitmentsResponseDto;
import com.example.sideproject.domain.recruitment.entity.QRecruitment;
import com.example.sideproject.domain.recruitment.entity.QRecruitmentTechStack;
import com.example.sideproject.domain.status.project.dto.StatusApplicantResponseDto;
import com.example.sideproject.domain.status.project.dto.StatusSearchRequest;
import com.example.sideproject.domain.techstack.dto.BasicTechStack;
import com.example.sideproject.domain.techstack.dto.TechStackMapping;
import com.example.sideproject.domain.techstack.dto.TechStackVo;
import com.example.sideproject.domain.techstack.repository.query.TechStackQueryParam;
import com.example.sideproject.domain.techstack.repository.query.TechStackQueryRepository;
import com.example.sideproject.global.enums.Position;
import com.example.sideproject.global.util.QueryUtil;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ApplicantQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final TechStackQueryRepository techStackQueryRepository;

    private final QApplicant qApplicant = QApplicant.applicant;
    private final QRecruitment qRecruitment = QRecruitment.recruitment;
    private final QRecruitmentTechStack qRecruitmentTechStack = QRecruitmentTechStack.recruitmentTechStack;

    public Page<StatusApplicantResponseDto> findApplications(Pageable pageable, Long userId, StatusSearchRequest searchRequest) {
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
                        statusSearchCondition(userId, searchRequest)
                )
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(QueryUtil.createOrderSpecifiers(pageable.getSort(), qApplicant))
                .fetch();


        TechStackQueryParam<BasicTechStack> queryParam = TechStackQueryParam.builder()
                .idPath(qRecruitmentTechStack.recruitment.id)
                .selectExpressions(
                        List.of(qRecruitmentTechStack.recruitment.id,
                                qRecruitmentTechStack.techStack.id,
                                qRecruitmentTechStack.techStack.name)
                )
                .entityPath(qRecruitmentTechStack)
                .joinPath(qRecruitmentTechStack.techStack)
                .build();

        List<StatusApplicantResponseDto> result = techStackQueryRepository.withTechStacks(queryParam, applications);

        return QueryUtil.createPage(jpaQueryFactory, qApplicant, result, pageable, statusSearchCondition(userId, searchRequest));
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

    private BooleanExpression statusSearchCondition(Long userId, StatusSearchRequest searchRequest) {
        return Expressions.allOf(eqApplicationUserId(userId), eqApplicationStatus(searchRequest.status()));
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

    private BooleanExpression eqApplicationStatus(ApplicationStatus status) {
        if (status == null) {
            return null;
        }
        return qApplicant.status.eq(status);
    }

}
