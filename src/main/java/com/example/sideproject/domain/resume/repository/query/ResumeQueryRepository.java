package com.example.sideproject.domain.resume.repository.query;

import com.example.sideproject.domain.pr.dto.PublicResumesResponseDto;
import com.example.sideproject.domain.resume.dto.ResumeListResponseDto;
import com.example.sideproject.domain.resume.entity.QResume;
import com.example.sideproject.domain.resume.entity.QResumeTechStack;
import com.example.sideproject.domain.resume.entity.Resume;
import com.example.sideproject.domain.techstack.dto.TechStackMappingDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ResumeQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private QResume qResume = QResume.resume;

    public List<ResumeListResponseDto> getResumes(Long userId) {
        List<ResumeListResponseDto> resumes = jpaQueryFactory.select(Projections.constructor(
                        ResumeListResponseDto.class,
                        qResume.id,
                        qResume.title,
                        qResume.position,
                        qResume.workType,
                        qResume.publishedAt,
                        qResume.modifiedAt
                )).from(qResume)
                .where(qResume.user.id.eq(userId))
                .orderBy(qResume.modifiedAt.desc())
                .fetch();

        List<Long> resumeIds = resumes.stream()
                .map(ResumeListResponseDto::getResumeId)
                .toList();

        List<TechStackMappingDto> techStacks = getTechStacks(resumeIds);

        Map<Long, List<TechStackMappingDto>> techStackMap = techStacks.stream()
                .collect(Collectors.groupingBy(TechStackMappingDto::id));

        List<ResumeListResponseDto> result = resumes.stream().map(resume -> resume.addTechStack(techStackMap.get(resume.getResumeId()))).toList();
        return result;
    }

    private List<TechStackMappingDto> getTechStacks(List<Long> resumeIds) {
        QResumeTechStack qResumeTechStack = QResumeTechStack.resumeTechStack;

        List<TechStackMappingDto> techStacks = jpaQueryFactory.select(Projections.constructor(
                        TechStackMappingDto.class,
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
