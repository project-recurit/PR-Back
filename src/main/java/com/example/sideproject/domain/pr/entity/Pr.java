package com.example.sideproject.domain.pr.entity;

import com.example.sideproject.domain.pr.dto.PrExperienceRequest;
import com.example.sideproject.domain.pr.dto.PrRequest;
import com.example.sideproject.domain.pr.dto.PrTechStackRequest;
import com.example.sideproject.domain.resume.entity.ResumeTechStack;
import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.entity.PostCount;
import com.example.sideproject.global.entity.Timestamped;
import com.example.sideproject.global.enums.Position;
import com.example.sideproject.global.enums.WorkType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@NamedEntityGraph(
        name = "Pr.withTechStacks",
        attributeNodes = {
                @NamedAttributeNode(value = "techStacks", subgraph = "techStacks.techStack"),
                @NamedAttributeNode(value = "user")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "techStacks.techStack",
                        attributeNodes = @NamedAttributeNode("techStack")
                )
        }
)
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
        this.experiences = Objects.requireNonNullElse(experiences, List.of());
        this.techStacks = Objects.requireNonNullElse(techStacks, List.of());
        addExperiences();
        addTechStacks();
    }

    public Pr(Long id) {
        this.id = id;
    }

    public void update(PrRequest req) {
        List<PrTechStack> prTechStack = req.techStacks().stream().map(PrTechStackRequest::toEntity).toList();
        title = req.title();
        position = req.position();
        introduce = req.introduce();
        workType = req.workType();
        if (Objects.nonNull(documentUrl)) {
            this.documentUrl = String.join(",", req.documentUrl());
        }
        updateExperience(req.experiences().stream().map(PrExperienceRequest::toEntity).toList());
        updatePrTechStack(prTechStack);
    }

    private void updateExperience(List<PrExperience> experiences) {
        int prevSize = this.experiences.size();
        int newSize = experiences.size();
        int index = 0;

        for (PrExperience experience : experiences) {
            // 이전 데이터가 있으면 업데이트
            if (index < prevSize) {
                PrExperience newExperience = this.experiences.get(index);
                newExperience.update(experience);
            } else {
                // 이전 데이터보다 많으면 새로 추가
                experience.addPr(this);
                this.experiences.add(experience);
            }
            index++;
        }

        // 이전 데이터가 더 많으면 삭제
        if (prevSize > newSize) {
            for (int i=1; i<= prevSize-newSize; i++) {
                this.experiences.remove(prevSize -i);
            }
        }
    }

    private void updatePrTechStack(List<PrTechStack> prTechStacks) {
        this.techStacks.clear();
        if (prTechStacks.isEmpty()) {
            return;
        }
        for (PrTechStack prTechStack : prTechStacks) {
            prTechStack.addPr(this);
            this.techStacks.add(prTechStack);
        }
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

    public boolean isOwner(Long userId) {
        return Objects.equals(user.getId(), userId);
    }

}
