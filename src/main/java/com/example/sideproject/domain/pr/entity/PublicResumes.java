package com.example.sideproject.domain.pr.entity;

import com.example.sideproject.domain.resume.entity.Resume;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Builder
@Getter
@Entity
public class PublicResumes extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "public_resume_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    private int viewCount;
    private int commentCount;
    private int favoriteCount;

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void decreaseViewCount() {
        if (this.viewCount > 0) {
            this.viewCount--;
        }
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void decreaseCommentCount() {
        if (this.commentCount > 0) {
            this.commentCount--;
        }
    }

    public void increaseFavoriteCount() {
        this.favoriteCount++;
    }

    public void decreaseFavoriteCount() {
        if (this.favoriteCount > 0) {
            this.favoriteCount++;
        }
    }

}
