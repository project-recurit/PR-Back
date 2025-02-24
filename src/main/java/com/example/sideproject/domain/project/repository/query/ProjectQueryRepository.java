package com.example.sideproject.domain.project.repository.query;

import com.example.sideproject.domain.project.dto.*;
import com.example.sideproject.domain.project.entity.QProject;
import com.example.sideproject.domain.project.entity.QProjectTechStack;
import com.example.sideproject.domain.project.entity.QProjectUrl;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProjectQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QProject project = QProject.project;
    QProjectTechStack projectTechStack = QProjectTechStack.projectTechStack;
    QUser user = QUser.user;
    QTechStack techStack = QTechStack.techStack;
    QProjectUrl projectUrl = QProjectUrl.projectUrl;
    private PageableExecutionUtils pageableExecutionUtils;

    public ProjectDetailResponseDto getProject(Long projectId) {

        queryFactory // 조회수 + 1
                .update(project)
                .set(project.viewCount, project.viewCount.add(1))
                .where(project.id.eq(projectId))
                .execute();

        ProjectDetailResponseDto detail = queryFactory // 구인 글 정보 조회
                .select(Projections.constructor(
                        ProjectDetailResponseDto.class,
                        project.id.as("id"),
                        project.title.as("title"),
                        project.content.as("content"),
                        project.expectedPeriod.as("expectedPeriod"),
                        project.viewCount.as("viewCount"),
                        project.commentCount.as("commentCount"),
                        user.nickname.as("userNickname"),
                        project.recruitmentPeriod.as("recruitmentPeriod"),
                        project.recruitStatus.stringValue().as("recruitStatus"),
                        project.teamSize.as("teamSize"),
                        project.modifiedAt.as("modifiedAt")
                ))
                .from(project)
                .join(project.user, user)
                .where(project.id.eq(projectId))
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
                .from(projectTechStack)
                .join(projectTechStack.techStack, techStack)
                .where(projectTechStack.project.id.eq(projectId))
                .fetch();

        List<ProjectUrlResponseDto> projectUrls = queryFactory // 구인 글 이미지 url 조회
                .select(Projections.constructor(
                        ProjectUrlResponseDto.class,
                        projectUrl.id.as("id"),
                        projectUrl.imageUrl.as("imageUrl")
                ))
                .from(projectUrl)
                .where(projectUrl.project.id.eq(projectId))
                .fetch();

        detail.setFileUrls(projectUrls); // dto 합치기
        detail.setTechStacks(techStacks);

        return detail;
    }

    public Page<ProjectsResponseDto> getProjects(Pageable pageable) {

        List<ProjectsResponseDto> projects = queryFactory
                .select(Projections.constructor(
                        ProjectsResponseDto.class,
                                project.id.as("id"),
                                project.title.as("title"),
                                user.nickname.as("userNickname"),
                                project.viewCount.as("viewCount"),
                                project.commentCount.as("commentCount"),
                                project.modifiedAt.as("modifiedAt")
                        )
                ).from(project)
                .join(project.user, user)
                .orderBy(project.modifiedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> projectIds = projects.stream().map(ProjectsResponseDto::getId).toList();

        List<ProjectTechStackDto> techStacks = queryFactory
                .select(Projections.constructor(
                        ProjectTechStackDto.class,
                        projectTechStack.project.id,
                        projectTechStack.techStack.id,
                        projectTechStack.techStack.name
                ))
                .from(projectTechStack)
                .join(projectTechStack.techStack)
                .where(projectTechStack.project.id.in(projectIds))
                .fetch();

        Map<Long, List<Map.Entry<Long, String>>> techStackMap = techStacks.stream()
                .collect(Collectors.groupingBy(
                        ProjectTechStackDto::getProjectId,
                        Collectors.mapping(dto -> Map.entry(dto.getTechStackId(), dto.getName()), Collectors.toList())
                ));

        List<ProjectsResponseDto> result = projects.stream()
                .map(project -> new ProjectsResponseDto(
                        project.getId(),
                        project.getTitle(),
                        project.getUserNickname(),
                        project.getViewCount(),
                        project.getCommentCount(),
                        project.getModifiedAt(),
                        techStackMap.getOrDefault(project.getId(), Collections.emptyList())
                                .stream()
                                .map(entry -> new TechStackDto(entry.getKey(), entry.getValue())) // DTO 변환
                                .toList()
                ))
                .toList();


        return PageableExecutionUtils.getPage(result, pageable, () -> countQuery().fetchOne());
    }
    private JPAQuery<Long> countQuery() {
        return queryFactory.select(project.count())
                .from(project);
    }

}
