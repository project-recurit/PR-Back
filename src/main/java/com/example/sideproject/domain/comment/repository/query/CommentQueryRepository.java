package com.example.sideproject.domain.comment.repository.query;

import com.example.sideproject.domain.comment.dto.CommentResponseDto;
import com.example.sideproject.domain.comment.dto.NestedCommentDto;
import com.example.sideproject.domain.comment.entity.QComment;
import com.example.sideproject.domain.project.entity.QProject;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {

    private final JPAQueryFactory queryFactory;
    QComment comment = QComment.comment;
    QProject project = QProject.project;

    public Page<NestedCommentDto> getComments(Long projectId, Pageable pageable) {
        // 부모 댓글 목록 조회
        List<CommentResponseDto> parentComments = queryFactory
                .select(Projections.constructor(
                        CommentResponseDto.class,
                        comment.id,
                        comment.content,
                        comment.modifiedAt,
                        comment.user.nickname
                ))
                .from(comment)
                .where(comment.project.id.eq(projectId).and(comment.parentId.isNull())) // 부모 댓글만 가져오기
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 부모 댓글 ID 리스트 추출
        List<Long> parentIds = parentComments.stream()
                .map(CommentResponseDto::getCommentId)
                .collect(Collectors.toList());

        // 부모 댓글이 없는 경우 바로 반환
        if (parentIds.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        // 자식 댓글 목록 조회 (부모 댓글 ID에 속한 모든 대댓글 가져오기)
        Map<Long, List<CommentResponseDto>> childCommentsMap = queryFactory
                .select(Projections.constructor(
                        CommentResponseDto.class,
                        comment.id,
                        comment.content,
                        comment.modifiedAt,
                        comment.user.nickname,
                        comment.parentId
                ))
                .from(comment)
                .where(comment.parentId.in(parentIds))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(CommentResponseDto::getParentId));

        // 부모 댓글과 자식 댓글을 매핑하여 NestedCommentDto 생성
        List<NestedCommentDto> nestedComments = parentComments.stream()
                .map(parent -> new NestedCommentDto(parent, childCommentsMap.getOrDefault(parent.getCommentId(), new ArrayList<>())))
                .collect(Collectors.toList());

        // 전체 부모 댓글 개수 조회 (count 쿼리 실행)
        long totalParentCount = queryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.project.id.eq(projectId).and(comment.parentId.isNull()))
                .fetchOne();

        // Page 객체로 변환하여 반환 (count 최적화)
        return PageableExecutionUtils.getPage(nestedComments, pageable, () -> totalParentCount);
    }

}
