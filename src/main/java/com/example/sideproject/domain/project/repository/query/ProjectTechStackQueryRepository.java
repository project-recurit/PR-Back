package com.example.sideproject.domain.project.repository.query;

import com.example.sideproject.domain.project.entity.QProjectTechStack;
import com.example.sideproject.domain.techstack.entity.QTechStack;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProjectTechStackQueryRepository {

    private final JPAQueryFactory queryFactory;
    private QProjectTechStack projectTechStack;
    private QTechStack techStack;

//    public void createProjectTechStack(Long projectId, List<Long> techStacks) {
//         insert = queryFactory.insert(projectTechStack)
//                .columns(projectTechStack.project, projectTechStack.techStack)
//                .select(
//                        JPAExpressions
//                                .select(techStack.id, Expressions.constant(projectId))
//                                .from(techStack)
//                                .where(techStack.id.in(techStacks))
//                );
//        insert.
//    }
}
