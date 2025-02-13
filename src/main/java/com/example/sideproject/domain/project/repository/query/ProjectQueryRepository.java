package com.example.sideproject.domain.project.repository.query;

import com.example.sideproject.domain.project.dto.ProjectResponseDto;
import com.example.sideproject.domain.project.dto.ProjectTechStackResponseDto;
import com.example.sideproject.domain.project.dto.ProjectsResponseDto;
import com.example.sideproject.domain.project.entity.ProjectTechStack;
import com.example.sideproject.domain.project.entity.QProject;
import com.example.sideproject.domain.project.entity.QProjectTechStack;
import com.example.sideproject.domain.techstack.entity.QTechStack;
import com.example.sideproject.domain.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProjectQueryRepository {

    private final JPAQueryFactory queryFactory;
    private PageableExecutionUtils pageableExecutionUtils;

    public ProjectResponseDto getProject(Long projectId) {

        QProject project = QProject.project;
        QUser user = QUser.user;
        JPAQuery<ProjectResponseDto> query = queryFactory
                .select(Projections.constructor(ProjectResponseDto.class,
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
                        )
                )
                .from(project)
                .join(project.user, user)
                .where(project.id.eq(projectId));

        return query.fetchOne();
    }

        public Page<ProjectsResponseDto> getProjects(Pageable pageable) {

        QProject project = QProject.project;
        QProjectTechStack projectTechStack = QProjectTechStack.projectTechStack;
        QTechStack techStack = QTechStack.techStack;
        QUser user = QUser.user;

        JPQLQuery<ProjectsResponseDto> query = queryFactory
                .select(Projections.constructor(
                                ProjectsResponseDto.class,
                                project.id.as("id"),
                                project.title.as("title"),
                                user.nickname.as("userNickname"),
                                project.viewCount.as("viewCount"),
                                project.likeCount.as("likeCount"),
                                project.modifiedAt.as("modifiedAt")
                        )
                ).from(project)
                .join(project.user, user)
                .join(project.projectTechStacks, projectTechStack)
                .leftJoin(projectTechStack.techStack, techStack)
                .orderBy(project.modifiedAt.desc())
                .groupBy(project.id)
                .limit(20);

        List<ProjectsResponseDto> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, query::fetchCount);
    }


}
