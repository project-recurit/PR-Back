package com.example.sideproject.domain.resume.entity;

import com.example.sideproject.domain.resume.dto.ExperienceRequest;
import com.example.sideproject.domain.resume.dto.ResumeRequest;
import com.example.sideproject.global.enums.Position;
import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.entity.Timestamped;
import com.example.sideproject.global.enums.WorkType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Resume extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Position position;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String introduce;

    private WorkType workType;

    private String documentUrl;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experiences;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeTechStack> resumeTechStacks;

    @Builder
    public Resume(Long id, User user, Position position, String title, String introduce, WorkType workType, List<String> documentUrl, List<Experience> experiences, List<TechStack> resumeTechStacks) {
        this.id = id;
        this.user = user;
        this.position = position;
        this.title = title;
        this.introduce = introduce;
        this.workType = workType;
        if (Objects.nonNull(documentUrl)) {
            this.documentUrl = String.join(",", documentUrl);
        }
        this.experiences = addExperiences(experiences);
        this.resumeTechStacks = addTechStack(resumeTechStacks);
    }

    public Resume(Long id) {
        this.id = id;
    }

    public List<ResumeTechStack> addTechStack(List<TechStack> techStacks) {
        List<ResumeTechStack> result = new ArrayList<>();
        for (TechStack techStack : techStacks) {
            ResumeTechStack resumeTechStack = ResumeTechStack.builder()
                    .resume(this)
                    .techStack(techStack)
                    .build();
            result.add(resumeTechStack);
        }
        return result;
    }

    public void update(ResumeRequest req) {
        List<TechStack> techStacks = req.techStackIds().stream().map(TechStack::new).toList();
        this.position = req.position();
        this.title = req.title();
        this.introduce = req.introduce();
        this.workType = req.workType();
        this.documentUrl = String.join(",", req.documentUrl());
        updateExperience(req.experiences().stream().map(ExperienceRequest::toEntity).toList());
        updateTechStack(techStacks);
    }

    public boolean canPublish() {
        return !title.isEmpty() && !introduce.isEmpty();
    }

    private void updateTechStack(List<TechStack> techStacks) {
        resumeTechStacks.clear();
        if (techStacks.isEmpty()) {
            return;
        }
        for (TechStack techStack : techStacks) {
            resumeTechStacks.add(new ResumeTechStack(techStack, this));
        }
    }

    /**
     * 프로젝트 경력 수정
     */
    private void updateExperience(List<Experience> experiences) {
        int prevSize = this.experiences.size();
        int newSize = experiences.size();
        int index = 0;

        for (Experience experience : experiences) {
            // 이전 데이터가 있으면 업데이트
            if (index < prevSize) {
                Experience newExperience = this.experiences.get(index);
                newExperience.update(experience);
            } else {
                // 이전 데이터보다 많으면 새로 추가
                experience.addResume(this);
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

    private List<Experience> addExperiences(List<Experience> experiences) {
        List<Experience> result = new ArrayList<>();
        if (experiences.isEmpty()) {
            return result;
        }

        for (Experience experience : experiences) {
            experience.addResume(this);
            result.add(experience);
        }
        return result;
    }

    public List<TechStack> getResumeTechStacks() {
        return resumeTechStacks.stream().map(ResumeTechStack::getTechStack).toList();
    }

    public List<String> getDocumentUrl() {
        if (documentUrl.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(documentUrl.split(",")).toList();
    }
}
