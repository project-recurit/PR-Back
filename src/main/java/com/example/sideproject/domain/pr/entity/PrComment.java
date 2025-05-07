package com.example.sideproject.domain.pr.entity;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class PrComment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pr_comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pr_id")
    private Pr pr;

    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private PrComment parent;

    private int replyCount;

    @Builder
    public PrComment(Long id, User user, Pr pr, String content, PrComment parent, Integer replyCount) {
        this.id = id;
        this.user = user;
        this.pr = pr;
        this.content = content;
        this.parent = parent;
        this.replyCount = replyCount == null? 0 : replyCount;
    }

    public PrComment(Long id) {
        this.id = id;
    }

    public void increaseCount() {
        pr.increaseCommentCount();
        if (parent == null) {
            return;
        }
        parent.replyCount++;
    }

    public void decreaseCount() {
        pr.decreaseCommentCount();
        if (parent == null) {
            return;
        }
        parent.replyCount--;
    }

    public void contentUpdate(String content) {
        this.content = content;
    }

    public boolean isOwner(Long userId) {
        return Objects.equals(user.getId(), userId);
    }

}
