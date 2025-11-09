package com.example.sideproject.domain.recruitment.repository.query;

import com.example.sideproject.domain.recruitment.dto.*;
import com.example.sideproject.domain.recruitment.entity.*;
import com.example.sideproject.domain.recruitment.entity.QRecruitment;
import com.example.sideproject.domain.recruitment.entity.QRecruitmentImage;
import com.example.sideproject.domain.recruitment.entity.QRecruitmentPosition;
import com.example.sideproject.domain.recruitment.entity.QRecruitmentTechStack;
import com.example.sideproject.domain.status.project.dto.StatusApplicantResponseDto;
import com.example.sideproject.domain.techstack.dto.BasicTechStack;
import com.example.sideproject.domain.techstack.dto.TechStackDto;
import com.example.sideproject.domain.techstack.dto.TechStackResponse;
import com.example.sideproject.domain.techstack.entity.QTechStack;
import com.example.sideproject.domain.techstack.repository.query.TechStackQueryParam;
import com.example.sideproject.domain.techstack.repository.query.TechStackQueryRepository;
import com.example.sideproject.domain.user.entity.QUser;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.enums.Position;
import com.example.sideproject.global.enums.WorkType;
import com.example.sideproject.global.exception.CustomException;
import com.example.sideproject.global.util.QueryUtil;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RecruitmentQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final TechStackQueryRepository techStackQueryRepository;

    QRecruitment recruitment = QRecruitment.recruitment;
    QRecruitmentTechStack recruitmentTechStack = QRecruitmentTechStack.recruitmentTechStack;
    QUser user = QUser.user;
    QTechStack techStack = QTechStack.techStack;
    QRecruitmentImage recruitmentImage = QRecruitmentImage.recruitmentImage;
    QRecruitmentPosition recruitmentPosition = QRecruitmentPosition.recruitmentPosition;

    private PageableExecutionUtils pageableExecutionUtils;

    public RecruitmentDetailResponseDto getRecruitment(Long recruitmentId) {

        increaseViewCount(recruitmentId); // 조회수 + 1

        RecruitmentDetailResponseDto detail = queryFactory // 구인 글 정보 조회
                .select(Projections.constructor(
                        RecruitmentDetailResponseDto.class,
                        recruitment.id,
                        recruitment.title,
                        user.nickname,           // nickname
                        user.profileUrl,         // profileUrl
                        recruitment.viewCount,
                        recruitment.commentCount,
                        recruitment.favoriteCount,
                        recruitment.createdAt.stringValue(), // createdAt
                        recruitment.modifiedAt.stringValue(), // modifiedAt
                        recruitment.content,
                        recruitment.estimatedDuration,
                        recruitment.deadLine.stringValue(),   // deadLine
                        recruitment.isRecruiting,
                        recruitment.workType.stringValue(),
                        recruitment.recruitmentCategory,
                        recruitment.isCommercial
                ))
                .from(recruitment)
                .join(recruitment.user, user)
                .where(recruitment.id.eq(recruitmentId))
                .fetchOne();

        if (detail == null) {
            throw new CustomException(ErrorType.PROJECT_RECRUIT_NOT_FOUND);
        }

        List<RecruitmentDetailResponseDto> withTechStacks = getResponseListWithTechStacks(List.of(detail));

        List<RecruitmentImageResponseDto> recruitmentImages = queryFactory // 구인 글 이미지 url 조회
                .select(Projections.constructor(
                        RecruitmentImageResponseDto.class,
                        recruitmentImage.id.as("id"),
                        recruitmentImage.imageUrl.as("imageUrl")
                ))
                .from(recruitmentImage)
                .where(recruitmentImage.recruitment.id.eq(recruitmentId))
                .fetch();

        List<RecruitmentPositionResponseDto> recruitmentPositions = queryFactory
                .select(
                        Projections.constructor(
                                RecruitmentPositionResponseDto.class,
                                recruitmentPosition.position.as("position"),
                                recruitmentPosition.capacity.as("capacity")
                        ))
                .from(recruitmentPosition)
                .where(recruitmentPosition.recruitment.id.eq(recruitmentId))
                .fetch();

        detail = withTechStacks.get(0);
        detail.setFileUrls(recruitmentImages); // dto 합치기
        detail.setRecruitPositions(recruitmentPositions);

        if (detail.getEstimatedDuration() != null) {
            detail.setEstimatedDurationDetail(detail.getEstimatedDuration().getDescription());
        }

        return detail;
    }

    private <T extends TechStackResponse> List<T> getResponseListWithTechStacks(List<T> detail) {
        TechStackQueryParam<BasicTechStack> queryParam = TechStackQueryParam.builder()
                .idPath(recruitmentTechStack.recruitment.id)
                .selectExpressions(
                        List.of(recruitmentTechStack.recruitment.id,
                                recruitmentTechStack.techStack.id,
                                recruitmentTechStack.techStack.name)
                )
                .entityPath(recruitmentTechStack)
                .joinPath(recruitmentTechStack.techStack)
                .build();

        return techStackQueryRepository.withTechStacks(queryParam, detail);
    }

    public Page<RecruitmentsResponseDto> getRecruitments(Pageable pageable, RecruitmentSearchDto searchDto) {

        List<RecruitmentsResponseDto> recruitments = queryFactory
                .select(Projections.constructor(
                                RecruitmentsResponseDto.class,
                                recruitment.id.as("id"),
                                recruitment.title.as("title"),
                                user.nickname.as("nickname"),
                                recruitment.viewCount.as("viewCount"),
                                recruitment.commentCount.as("commentCount"),
                                recruitment.modifiedAt.as("modifiedAt"),
                                recruitment.recruitmentCategory.as("recruitmentCategory"),
                                recruitment.isCommercial.as("isCommercial"),
                                recruitment.workType.as("workType")
                        )
                ).from(recruitment)
                .join(recruitment.user, user)
                .where(searchRecruitment(searchDto))
                .orderBy(recruitment.modifiedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<RecruitmentsResponseDto> result = getResponseListWithTechStacks(recruitments);

        return QueryUtil.createPage(queryFactory, recruitment, result, pageable, null);
    }

    private JPAQuery<Long> countQuery() {
        return queryFactory.select(recruitment.count())
                .from(recruitment);
    }

    public List<Recruitment> findAllWithTechStacks() {
        // 프로젝트 ID 목록 조회
        List<Long> recruitmentIds = queryFactory
                .select(recruitment.id)
                .from(recruitment)
                .fetch();

        if (recruitmentIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 프로젝트 조회
        List<Recruitment> recruitments = queryFactory
                .selectFrom(recruitment)
                .where(recruitment.id.in(recruitmentIds))
                .fetch();

        // 프로젝트 ID별 기술 스택 조회
        Map<Long, List<RecruitmentTechStack>> techStacksByRecruitmentId = queryFactory
                .selectFrom(recruitmentTechStack)
                .join(recruitmentTechStack.techStack, techStack).fetchJoin()
                .where(recruitmentTechStack.recruitment.id.in(recruitmentIds))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(pts -> pts.getRecruitment().getId()));

        // 각 프로젝트에 기술 스택 설정
        recruitments.forEach(p -> {
            List<RecruitmentTechStack> techStacks = techStacksByRecruitmentId.getOrDefault(p.getId(), Collections.emptyList());
            // 기존 컬렉션 초기화 후 추가
            p.getRecruitmentTechStacks().clear();
            p.getRecruitmentTechStacks().addAll(techStacks);
        });

        return recruitments;
    }

    private void increaseViewCount(Long recruitmentId) {
        queryFactory // 조회수 + 1
                .update(recruitment)
                .set(recruitment.viewCount, recruitment.viewCount.add(1))
                .where(recruitment.id.eq(recruitmentId))
                .execute();
    }

    private BooleanExpression searchRecruitment(RecruitmentSearchDto recruitmentSearchDto) {
        List<Long> techStacks = recruitmentSearchDto.getTechStacks();
        List<Position> positions = recruitmentSearchDto.getPositions();
        List<WorkType> workTypes = recruitmentSearchDto.getWorkType();

        BooleanExpression techStacksIn = null;
        BooleanExpression positionIn = null;
        BooleanExpression workTypeIn = null;

        if (!techStacks.isEmpty()) {
            techStacksIn = recruitment.recruitmentTechStacks.any().techStack.id.in(techStacks);
        }

        if (!positions.isEmpty()) {
            positionIn = recruitment.positions.any().position.in(positions);
        }

        if (!workTypes.isEmpty()) {
            workTypeIn = recruitment.workType.in(workTypes);
        }
        return Expressions.allOf(techStacksIn, positionIn, workTypeIn);
    }
}
