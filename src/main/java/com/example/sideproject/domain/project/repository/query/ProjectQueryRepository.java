package com.example.sideproject.domain.project.repository.query;

import com.example.sideproject.domain.project.dto.*;
import com.example.sideproject.domain.project.entity.QProject;
import com.example.sideproject.domain.project.entity.QProjectTechStack;
import com.example.sideproject.domain.project.entity.QProjectUrl;
import com.example.sideproject.domain.techstack.entity.QTechStack;
import com.example.sideproject.domain.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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

        JPAQuery<ProjectResponseDto> query = queryFactory
                .select(Projections.constructor(
                        ProjectResponseDto.class,
                        project.id.as("id"),
                        project.title.as("title"),
                        project.content.as("content"),
                        project.expectedPeriod.as("expectedPeriod"),
                        project.viewCount.as("viewCount"),
                        project.likeCount.as("likeCount"),
                        user.nickname.as("userNickname"),
                        project.recruitmentPeriod.as("recruitmentPeriod"),
                        project.recruitStatus.stringValue().as("recruitStatus"),
                        project.teamSize.as("teamSize"),
                        project.modifiedAt.as("modifiedAt")
                ))
                .from(project)
                .join(project.user, user)
                .where(project.id.eq(projectId));

        ProjectResponseDto projectResponseDto = query.fetchOne();

        List<ProjectTechStackResponseDto> techStacks = queryFactory
                .select(Projections.constructor(
                        ProjectTechStackResponseDto.class,
                        techStack.id.as("id"),
                        techStack.name.as("name")
                ))
                .from(projectTechStack)
                .join(projectTechStack.techStack, techStack)
                .where(projectTechStack.project.id.eq(projectId))
                .fetch();

        List<ProjectUrlResponseDto> projectUrls = queryFactory
                .select(Projections.constructor(
                        ProjectUrlResponseDto.class,
                        projectUrl.id.as("id"),
                        projectUrl.imageUrl.as("imageUrl")
                ))
                .from(projectUrl)
                .where(projectUrl.project.id.eq(projectId))
                .fetch();


        return new ProjectDetailResponseDto(
                projectResponseDto,
                projectUrls,
                techStacks
        );
    }

    public Page<ProjectsResponseDto> getProjects(Pageable pageable) {

        List<ProjectsQueryDto> projects = queryFactory
                .select(Projections.constructor(
                        ProjectsQueryDto.class,
                                project.id.as("id"),
                                project.title.as("title"),
                                user.nickname.as("userNickname"),
                                project.viewCount.as("viewCount"),
                                project.likeCount.as("likeCount"),
                                project.modifiedAt.as("modifiedAt")
                        )
                ).from(project)
                .join(project.user, user)
                .orderBy(project.modifiedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> projectIds = projects.stream().map(ProjectsQueryDto::id).toList();

        List<ProjectsTechStackResponseDto> techStacks = queryFactory
                .select(Projections.constructor(
                        ProjectsTechStackResponseDto.class,
                        projectTechStack.project.id.as("projectId"),
                        projectTechStack.techStack.id.as("id"),
                        projectTechStack.techStack.name.as("name")
                ))
                .from(projectTechStack)
                .join(projectTechStack.techStack)
                .where(projectTechStack.project.id.in(projectIds))
                .fetch();

        Map<Long, List<ProjectsTechStackResponseDto>> techStackMap = techStacks.stream()
                .collect(Collectors.groupingBy(ProjectsTechStackResponseDto::projectId));

        List<ProjectsResponseDto> result = projects.stream()
                .map(project -> new ProjectsResponseDto(
                        project.id(),
                        project.title(),
                        project.userNickname(),
                        project.viewCount(),
                        project.likeCount(),
                        project.modifiedAt(),
                        techStackMap.getOrDefault(project.id(), new ArrayList<>())
                ))
                .toList();

        return PageableExecutionUtils.getPage(result, pageable, () -> countQuery().fetchOne());
    }
    private JPAQuery<Long> countQuery() {
        return queryFactory.select(project.count())
                .from(project);
    }

}
