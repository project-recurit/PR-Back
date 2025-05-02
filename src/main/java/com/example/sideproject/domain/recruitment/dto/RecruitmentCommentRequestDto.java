package com.example.sideproject.domain.recruitment.dto;

import com.example.sideproject.domain.recruitment.entity.Recruitment;
import com.example.sideproject.domain.recruitment.entity.RecruitmentComment;
import com.example.sideproject.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;

public record RecruitmentCommentRequestDto(
        Long parentId,
        @NotBlank(message = "댓글 내용은 필수 기입 항목입니다.") String content
) {

    public RecruitmentComment toEntity(User user, Recruitment recruitment) {
        return RecruitmentComment.builder()
                .content(content)
                .parentId(parentId)
                .user(user)
                .recruitment(recruitment)
                .build();
    }
}
