package com.example.sideproject.domain.resume.repository.query;

import com.example.sideproject.domain.resume.dto.ResumeListResponse;
import com.example.sideproject.domain.resume.entity.QResume;
import com.example.sideproject.domain.resume.entity.QResumeTechStack;
import com.example.sideproject.domain.techstack.dto.TechStackVo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ResumeQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QResume qResume = QResume.resume;

    public List<ResumeListResponse> getResumes(Long userId) {
        List<ResumeListResponse> resumes = jpaQueryFactory.select(Projections.constructor(
                        ResumeListResponse.class,
                        qResume.id,
                        qResume.title,
                        qResume.position,
                        qResume.workType,
                        qResume.modifiedAt,
                        qResume.createdAt
                )).from(qResume)
                .where(qResume.user.id.eq(userId))
                .orderBy(qResume.modifiedAt.desc())
                .fetch();

        List<Long> resumeIds = resumes.stream()
                .map(ResumeListResponse::getId)
                .toList();

        List<TechStackVo> techStacks = getTechStacks(resumeIds);

        Map<Long, List<TechStackVo>> techStackMap = techStacks.stream()
                .collect(Collectors.groupingBy(TechStackVo::id));

        List<ResumeListResponse> result = resumes.stream().map(resume -> resume.addTechStack(techStackMap.get(resume.getId()))).toList();
        return result;
    }

    private List<TechStackVo> getTechStacks(List<Long> resumeIds) {
        QResumeTechStack qResumeTechStack = QResumeTechStack.resumeTechStack;

        List<TechStackVo> techStacks = jpaQueryFactory.select(Projections.constructor(
                        TechStackVo.class,
                        qResumeTechStack.resume.id,
                        qResumeTechStack.techStack.id,
                        qResumeTechStack.techStack.name
                ))
                .from(qResumeTechStack)
                .join(qResumeTechStack.techStack)
                .where(
                        qResumeTechStack.resume.id.in(resumeIds)
                )
                .fetch();
        return techStacks;
    }

}
