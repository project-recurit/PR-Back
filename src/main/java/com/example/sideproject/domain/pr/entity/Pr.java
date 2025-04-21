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
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Pr extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pr_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String introduce;
    private Position position;
    private WorkType workType;
    private String documentUrl;

    @OneToMany(mappedBy = "pr", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrExperience> experiences;

    @OneToMany(mappedBy = "pr", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrTechStack> techStacks;

    private PostCount count;

    @Builder
    public Pr(Long id,
              User user,
              String title,
              String introduce,
              Position position,
              WorkType workType,
              List<String> documentUrl,
              List<PrExperience> experiences,
              List<PrTechStack> techStacks,
              PostCount count) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.introduce = introduce;
        this.position = position;
        this.workType = workType;
        if (Objects.nonNull(documentUrl)) {
            this.documentUrl = String.join(",", documentUrl);
        }
        this.count = count == null? PostCount.init() : count;
        this.experiences = experiences;
        this.techStacks = techStacks;
        addExperiences();
        addTechStacks();
    }

    public void addExperiences() {
        for (PrExperience experience : experiences) {
            experience.addPr(this);
        }
    }

    public void addTechStacks() {
        for (PrTechStack techStack : techStacks) {
            techStack.addPr(this);
        }
    }

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
