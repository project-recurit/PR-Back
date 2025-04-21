package com.example.sideproject.domain.pr.entity;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.entity.PostCount;
import com.example.sideproject.global.entity.Timestamped;
import com.example.sideproject.global.enums.Position;
import com.example.sideproject.global.enums.WorkType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Pr extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "public_resume_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String introduce;
    private Position position;
    private WorkType workType;
    private String documentUrl;

    @OneToMany(mappedBy = "pr", fetch = FetchType.LAZY)
    private List<PrExperience> experiences;

    private PostCount count;

    public void increaseViewCount() {
        this.count.increaseViewCount();
    }

    public void increaseCommentCount() {
        this.count.increaseCommentCount();
    }

    public void decreaseCommentCount() {
        this.count.decreaseCommentCount();
    }

    public void increaseFavoriteCount() {
        this.count.increaseFavoriteCount();
    }

    public void decreaseFavoriteCount() {
        this.count.decreaseFavoriteCount();
    }

    public List<String> getDocumentUrl() {
        if (documentUrl.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(documentUrl.split(",")).toList();
    }

}
