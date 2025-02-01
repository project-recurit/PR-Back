package com.example.sideproject.domain.applicant.repository.query;

import com.example.sideproject.domain.applicant.dto.ApplicantResponseDto;
import com.example.sideproject.domain.applicant.dto.search.SearchApplicantDto;
import com.example.sideproject.domain.applicant.entity.ApplicationStatus;
import com.example.sideproject.domain.applicant.entity.QApplicant;
import com.example.sideproject.domain.user.entity.TechStack;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ApplicantQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private QApplicant qApplicant = QApplicant.applicant;

    public List<ApplicantResponseDto> findApplicants(Long userId, Long projectId, SearchApplicantDto searchDto) {
        List<ApplicantResponseDto> result = jpaQueryFactory.select(Projections.constructor(
                        ApplicantResponseDto.class,
                        qApplicant.id,
                        qApplicant.project.id,
                        qApplicant.user.nickname,
                        qApplicant.status,
                        qApplicant.createdAt,
                        qApplicant.modifiedAt
                )).from(qApplicant)
                .join(qApplicant.project)
                .join(qApplicant.user)
                .where(
                        eqProjectId(projectId),
                        eqProjectUserId(userId),
                        createSearchCondition(searchDto)
                ).fetch();

        return result;
    }

    private BooleanExpression createSearchCondition(SearchApplicantDto searchDto) {
        String nickname = searchDto.nickname();
        String position = searchDto.position();
        ApplicationStatus status = searchDto.status();

        BooleanExpression result = Expressions.asString("1").eq("1");

        if (nickname != null) {
            result = result.and(qApplicant.user.nickname.like(nickname));
        }

        if (position != null) {
            // position 관련 쿼리 추가
        }

        if (status != null) {
            // techStack 관련 쿼리 추가
            result = result.and(qApplicant.status.eq(status));
        }

        return result;
    }

    private BooleanExpression eqProjectUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        return qApplicant.project.user.id.eq(userId);
    }

    private BooleanExpression eqProjectId(Long projectId) {
        if (projectId == null) {
            return null;
        }
        return qApplicant.project.id.eq(projectId);
    }
}
