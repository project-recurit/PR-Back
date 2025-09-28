package com.example.sideproject.domain.techstack.repository.query;

import com.example.sideproject.domain.pr.dto.PrTechStackResponse;
import com.example.sideproject.domain.recruitment.dto.RecruitmentTechStackDto;
import com.example.sideproject.domain.recruitment.entity.QRecruitment;
import com.example.sideproject.domain.recruitment.entity.QRecruitmentTechStack;
import com.example.sideproject.domain.techstack.dto.TechStackDto;
import com.example.sideproject.domain.techstack.entity.QTechStack;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TechStackQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Map<Long, List<TechStackDto>> getTechStacks(List<Long> recruitmentIds, QRecruitmentTechStack recruitmentTechStack) {
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

        return techStacks.stream()
                .collect(Collectors.groupingBy(
                        RecruitmentTechStackDto::getRecruitmentId,
                        Collectors.mapping(
                                entry -> new TechStackDto(entry.getTechStackId(), entry.getName()),
                                Collectors.toList()
                        ))
                );
    }
}
