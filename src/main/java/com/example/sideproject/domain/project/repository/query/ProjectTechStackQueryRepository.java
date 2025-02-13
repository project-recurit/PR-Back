package com.example.sideproject.domain.project.repository.query;

import com.example.sideproject.domain.project.dto.ProjectTechStackResponseDto;
import com.example.sideproject.domain.project.entity.QProjectTechStack;
import com.example.sideproject.domain.techstack.entity.QTechStack;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProjectTechStackQueryRepository {

    private final JPAQueryFactory queryFactory;


    public List<ProjectTechStackResponseDto> getProjectTechStacks(Long projectId) {
        QProjectTechStack projectTechStack = QProjectTechStack.projectTechStack;
        QTechStack techStack = QTechStack.techStack;

        return queryFactory
                .select(Projections.constructor(
                        ProjectTechStackResponseDto.class,
                        techStack.id,
                        techStack.name
                ))
                .from(projectTechStack)
                .join(projectTechStack.techStack, techStack)
                .where(projectTechStack.project.id.eq(projectId))
                .fetch();
    }
}
