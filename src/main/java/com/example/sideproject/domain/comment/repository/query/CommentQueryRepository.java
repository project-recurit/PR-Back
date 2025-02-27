package com.example.sideproject.domain.comment.repository.query;

import com.example.sideproject.domain.comment.dto.CommentResponseDto;
import com.example.sideproject.domain.comment.entity.QComment;
import com.example.sideproject.domain.project.entity.QProject;
import com.querydsl.core.types.Projections;
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
public class CommentQueryRepository {

    private final JPAQueryFactory queryFactory;
    QComment comment = QComment.comment;
    QProject project = QProject.project;

    public Page<CommentResponseDto> getComments(Long projectId, Pageable pageable) {
        // 부모 댓글 목록 조회
        List<CommentResponseDto> comments = queryFactory
                .select(Projections.constructor(
                        CommentResponseDto.class,
                        comment.id,
                        comment.content,
                        comment.user.nickname,
                        comment.modifiedAt
                ))
                .from(comment)
                .where(comment.project.id.eq(projectId).and(comment.parentId.isNull())) // 부모 댓글만 가져오기
                .orderBy(comment.modifiedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Page 객체로 변환하여 반환 (count 최적화)
        return PageableExecutionUtils.getPage(comments, pageable, () -> countQuery().fetchOne());
    }

    public List<CommentResponseDto> getReply(Long commentId) {
        List<CommentResponseDto> reply = queryFactory.select(
                Projections.constructor(CommentResponseDto.class,
                        comment.id,
                        comment.content,
                        comment.user.nickname,
                        comment.modifiedAt
                )).from(comment)
                .where(comment.parentId.eq(commentId))
                .fetch();

        return reply;
    }

    private JPAQuery<Long> countQuery() {
        return queryFactory.select(comment.count())
                .from(comment);
    }
}
