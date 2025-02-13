package com.example.sideproject.domain.project.repository.query;

import com.example.sideproject.domain.project.dto.ProjectUrlResponseDto;
import com.example.sideproject.domain.project.entity.QProject;
import com.example.sideproject.domain.project.entity.QProjectUrl;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProjectUrlQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<ProjectUrlResponseDto> getUrls(Long projectId) {
        QProjectUrl projectUrl = QProjectUrl.projectUrl;

        return queryFactory.select(Projections.constructor(
                        ProjectUrlResponseDto.class,
                        projectUrl.id,
                        projectUrl.imageUrl
                )).from(projectUrl)
                .where(projectUrl.project.id.eq(projectId))
                .fetch();
    }
}
