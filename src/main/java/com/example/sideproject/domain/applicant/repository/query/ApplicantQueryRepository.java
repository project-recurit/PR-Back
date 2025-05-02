package com.example.sideproject.domain.applicant.repository.query;

import com.example.sideproject.domain.applicant.dto.ApplicantResponseDto;
import com.example.sideproject.domain.applicant.dto.search.SearchApplicantDto;
import com.example.sideproject.domain.applicant.entity.ApplicationStatus;
import com.example.sideproject.domain.applicant.entity.QApplicant;
import com.example.sideproject.global.enums.Position;
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
