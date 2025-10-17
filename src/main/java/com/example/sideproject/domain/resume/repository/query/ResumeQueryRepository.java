package com.example.sideproject.domain.resume.repository.query;

import com.example.sideproject.domain.resume.dto.ResumeListResponse;
import com.example.sideproject.domain.resume.entity.QResume;
import com.example.sideproject.domain.resume.entity.QResumeTechStack;
import com.example.sideproject.domain.status.project.dto.StatusApplicantResponseDto;
import com.example.sideproject.domain.techstack.dto.BasicTechStack;
import com.example.sideproject.domain.techstack.dto.TechStackResponse;
import com.example.sideproject.domain.techstack.dto.TechStackVo;
import com.example.sideproject.domain.techstack.repository.query.TechStackQueryParam;
import com.example.sideproject.domain.techstack.repository.query.TechStackQueryRepository;
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
    private final TechStackQueryRepository techStackQueryRepository;

    private final QResume qResume = QResume.resume;
    private final QResumeTechStack qResumeTechStack = QResumeTechStack.resumeTechStack;

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

        TechStackQueryParam<BasicTechStack> queryParam = TechStackQueryParam.builder()
                .idPath(qResumeTechStack.resume.id)
                .selectExpressions(
                        List.of(qResumeTechStack.resume.id,
                                qResumeTechStack.techStack.id,
                                qResumeTechStack.techStack.name)
                )
                .entityPath(qResumeTechStack)
                .joinPath(qResumeTechStack.techStack)
                .build();

        List<ResumeListResponse> result = techStackQueryRepository.withTechStacks(queryParam, resumes);

        return result;
    }
}
