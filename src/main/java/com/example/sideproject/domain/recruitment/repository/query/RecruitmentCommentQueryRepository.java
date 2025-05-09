package com.example.sideproject.domain.recruitment.repository.query;

import com.example.sideproject.domain.recruitment.dto.RecruitmentCommentResponseDto;
import com.example.sideproject.domain.recruitment.entity.QRecruitment;
import com.example.sideproject.domain.recruitment.entity.QRecruitmentComment;
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
public class RecruitmentCommentQueryRepository {

    private final JPAQueryFactory queryFactory;
    QRecruitmentComment comment = QRecruitmentComment.recruitmentComment;
    QRecruitment recruitment = QRecruitment.recruitment;

    public Page<RecruitmentCommentResponseDto> getComments(Long recruitmentId, Pageable pageable) {
        // 부모 댓글 목록 조회
        List<RecruitmentCommentResponseDto> comments = queryFactory
                .select(Projections.constructor(
                        RecruitmentCommentResponseDto.class,
                        comment.id,
                        comment.content,
                        comment.user.nickname,
                        comment.user.profileUrl,
                        comment.replyCount,
                        comment.createdAt.stringValue(),
                        comment.modifiedAt.stringValue()
                ))
                .from(comment)
                .where(comment.recruitment.id.eq(recruitmentId).and(comment.parentId.isNull())) // 부모 댓글만 가져오기
                .orderBy(comment.modifiedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Page 객체로 변환하여 반환 (count 최적화)
        return PageableExecutionUtils.getPage(comments, pageable, () -> countQuery().fetchOne());
    }

    public List<RecruitmentCommentResponseDto> getReply(Long commentId) {
        List<RecruitmentCommentResponseDto> reply = queryFactory.select(
                Projections.constructor(RecruitmentCommentResponseDto.class,
                        comment.id,
                        comment.content,
                        comment.user.nickname,
                        comment.user.profileUrl,
                        comment.replyCount,
                        comment.createdAt,
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
