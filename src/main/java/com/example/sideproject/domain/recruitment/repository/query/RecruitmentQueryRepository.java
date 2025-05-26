package com.example.sideproject.domain.recruitment.repository.query;

import com.example.sideproject.domain.recruitment.dto.*;
import com.example.sideproject.domain.recruitment.entity.*;
import com.example.sideproject.domain.techstack.dto.TechStackDto;
import com.example.sideproject.domain.techstack.entity.QTechStack;
import com.example.sideproject.domain.user.entity.QUser;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import com.querydsl.core.types.Projections;
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
    private final QRecruitment recruitment = QRecruitment.recruitment;
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

        List<TechStackDto> techStacks = queryFactory // 구인 글 기술 스택 조회
                .select(Projections.constructor(
                        TechStackDto.class,
                        techStack.id.as("id"),
                        techStack.name.as("name")
                ))
                .from(recruitmentTechStack)
                .join(recruitmentTechStack.techStack, techStack)
                .where(recruitmentTechStack.recruitment.id.eq(recruitmentId))
                .fetch();

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

        detail.setFileUrls(recruitmentImages); // dto 합치기
        detail.setTechStacks(techStacks);
        detail.setRecruitPositions(recruitmentPositions);

        if (detail != null && detail.getEstimatedDuration() != null) {
            detail.setEstimatedDurationDetail(detail.getEstimatedDuration().getDescription());
        }

        return detail;
    }

    public Page<RecruitmentsResponseDto> getRecruitments(Pageable pageable) {

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
                                recruitment.isCommercial.as("isCommercial")
                        )
                ).from(recruitment)
                .join(recruitment.user, user)
                .orderBy(recruitment.modifiedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> recruitmentIds = recruitments.stream().map(RecruitmentsResponseDto::getId).toList();

        List<RecruitmentTechStackDto> techStacks = queryFactory
                .select(Projections.constructor(
                        RecruitmentTechStackDto.class,
                        recruitmentTechStack.recruitment.id,
                        recruitmentTechStack.techStack.id,
                        recruitmentTechStack.techStack.name
                ))
                .from(recruitmentTechStack)
                .join(recruitmentTechStack.techStack)
                .where(recruitmentTechStack.recruitment.id.in(recruitmentIds))
                .fetch();

        Map<Long, List<Map.Entry<Long, String>>> techStackMap = techStacks.stream()
                .collect(Collectors.groupingBy(
                        RecruitmentTechStackDto::getRecruitmentId,
                        Collectors.mapping(dto -> Map.entry(dto.getTechStackId(), dto.getName()), Collectors.toList())
                ));

        List<RecruitmentsResponseDto> result = recruitments.stream()
                .map(recruitment -> new RecruitmentsResponseDto(
                        recruitment.getId(),
                        recruitment.getTitle(),
                        recruitment.getNickname(),
                        recruitment.getViewCount(),
                        recruitment.getCommentCount(),
                        recruitment.getModifiedAt(),
                        recruitment.getRecruitmentCategory(),
                        recruitment.isCommercial(),
                        techStackMap.getOrDefault(recruitment.getId(), Collections.emptyList())
                                .stream()
                                .map(entry -> new TechStackDto(entry.getKey(), entry.getValue())) // DTO 변환
                                .toList()
                ))
                .toList();


        return PageableExecutionUtils.getPage(result, pageable, () -> countQuery().fetchOne());
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
}
