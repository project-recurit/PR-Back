package com.example.sideproject.domain.recruitment.entity;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class RecruitmentComment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = true, name = "parent_id")
    private Long parentId;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "recruitment_id", nullable = false)
    private Recruitment recruitment;

    @Column(name = "reply_count")
    private int replyCount;

    @Builder
    public RecruitmentComment(String content, Long parentId, User user, Recruitment recruitment, int replyCount) {
        this.content = content;
        this.parentId = parentId;
        this.user = user;
        this.recruitment = recruitment;
        this.replyCount = replyCount;
    }

    public void update(String content) {
        this.content = content;
    }

    public void increaseReplyCount() {
        this.replyCount = this.replyCount + 1;
    }
}
