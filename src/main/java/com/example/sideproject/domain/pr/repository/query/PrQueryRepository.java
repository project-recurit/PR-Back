package com.example.sideproject.domain.pr.repository.query;

import com.example.sideproject.domain.pr.dto.PrListResponseDto;
import com.example.sideproject.domain.pr.dto.PrSearchRequest;
import com.example.sideproject.domain.pr.dto.PrTechStackVo;
import com.example.sideproject.domain.pr.dto.PrTechStackResponse;
import com.example.sideproject.domain.pr.entity.Pr;
import com.example.sideproject.domain.pr.entity.QPr;
import com.example.sideproject.domain.pr.entity.QPrTechStack;
import com.example.sideproject.domain.status.project.dto.StatusApplicantResponseDto;
import com.example.sideproject.domain.techstack.dto.BasicTechStack;
import com.example.sideproject.domain.techstack.repository.query.TechStackQueryParam;
import com.example.sideproject.domain.techstack.repository.query.TechStackQueryRepository;
import com.example.sideproject.global.enums.Position;
import com.example.sideproject.global.enums.WorkType;
import com.example.sideproject.global.util.QueryUtil;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
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
public class PrQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final TechStackQueryRepository techStackQueryRepository;

    private final QPr qPr = QPr.pr;
    private final QPrTechStack qPrTechStack = QPrTechStack.prTechStack;

    public Page<PrListResponseDto> getPrs(Pageable pageable, PrSearchRequest prSearchRequest) {
        List<PrListResponseDto> prs = jpaQueryFactory.select(Projections.constructor(
                        PrListResponseDto.class,
                        qPr.id,
                        qPr.title,
                        qPr.user.nickname,
                        qPr.user.profileUrl,
                        qPr.count.viewCount,
                        qPr.count.commentCount,
                        qPr.count.favoriteCount,
                        qPr.createdAt,
                        qPr.modifiedAt,
                        qPr.workType,
                        qPr.position
                )).from(qPr)
                .where(prSearchCondition(prSearchRequest))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(QueryUtil.createOrderSpecifiers(pageable.getSort(), qPr))
                .fetch();

        TechStackQueryParam<BasicTechStack> queryParam = TechStackQueryParam.builder()
                .idPath(qPrTechStack.pr.id)
                .mappingClass(PrTechStackVo.class)
                .selectExpressions(
                        List.of(qPrTechStack.pr.id,
                                qPrTechStack.techStack.id,
                                qPrTechStack.techStack.name,
                                qPrTechStack.level))
                .entityPath(qPrTechStack)
                .joinPath(qPrTechStack.techStack)
                .resultMapper(entry -> new PrTechStackResponse((PrTechStackVo) entry))
                .build();

        List<PrListResponseDto> result = techStackQueryRepository.withTechStacks(queryParam, prs);

        return QueryUtil.createPage(jpaQueryFactory, qPr, result, pageable, prSearchCondition(prSearchRequest));
    }

    private BooleanExpression prSearchCondition(PrSearchRequest prSearchRequest) {
        List<Position> positions = prSearchRequest.positions();
        List<Long> techStackIds = prSearchRequest.techStackIds();
        List<WorkType> workTypes = prSearchRequest.workTypes();

        BooleanExpression positionIn = null;
        BooleanExpression workTypeIn = null;
        BooleanExpression idIn = null;

        if (!positions.isEmpty()) {
            positionIn = qPr.position.in(positions);
        }

        if (!workTypes.isEmpty()) {
            workTypeIn = qPr.workType.in(workTypes);
        }

        List<Long> prIds = findIdsByTechStackIds(techStackIds);
        if (prIds != null) {
            idIn = qPr.id.in(prIds);
        }

        return Expressions.allOf(positionIn, workTypeIn, idIn, containsBySearchText(prSearchRequest.searchText()));
    }

    private BooleanExpression containsBySearchText(String searchText) {
        if (searchText == null) {
            return null;
        }
        return qPr.title.contains(searchText).or(qPr.introduce.contains(searchText));
    }

    private List<Long> findIdsByTechStackIds(List<Long> techStackIds) {
        if (techStackIds.isEmpty()) {
            return null;
        }


        return jpaQueryFactory.selectDistinct(qPrTechStack.pr.id)
                .from(qPrTechStack)
                .where(qPrTechStack.techStack.id.in(techStackIds))
                .fetch();
    }
}