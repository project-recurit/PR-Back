package com.example.sideproject.domain.project.entity;

import com.example.sideproject.domain.chat.entity.ChatRoom;
import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.entity.UserTechStack;
import com.example.sideproject.global.entity.Timestamped;
import com.example.sideproject.global.enums.WorkType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
public class Project extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = true, name = "dead_line") // 모집 기간
    private String deadLine;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectUrl> fileUrls = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectTechStack> projectTechStacks = new ArrayList<>();

    @Column(nullable = false, name = "view_count")
    private int viewCount;

    @Column(nullable = false, name = "comment_count")
    private int commentCount;

    @Column(nullable = false, name = "favorite_count")
    private int favoriteCount;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private User user;

    @Column(nullable = false, name = "estimated_duration") // 예상 기간
    @Enumerated(EnumType.STRING)
    private EstimatedDuration estimatedDuration;

    @Column(nullable = false, name = "is_recruiting") // 진행 여부
    private boolean isRecruiting;

    @Column(nullable = false, name = "recruitment_capacity") // 모집 인원
    private int recruitmentCapacity;

    @Column(nullable = false, name = "work_type") // 진행 방식
    @Enumerated(EnumType.STRING)
    private WorkType workType;

    @Builder
    public Project(String title, String content, String deadLine,
                   EstimatedDuration estimatedDuration, boolean isRecruiting, int recruitmentCapacity,
                   WorkType workType, User user, List<ProjectTechStack> projectTechStacks,
                   List<ProjectUrl> projectUrls, int viewCount, int commentCount, int favoriteCount,Long id) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.deadLine = deadLine;
        this.estimatedDuration = estimatedDuration;
        this.isRecruiting = isRecruiting;
        this.recruitmentCapacity = recruitmentCapacity;
        this.workType = workType;
        this.user = user;
        this.projectTechStacks = projectTechStacks != null ? projectTechStacks : new ArrayList<>();
        this.fileUrls = projectUrls != null ? projectUrls : new ArrayList<>();
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.favoriteCount = favoriteCount;
    }

    public void addCommentCount() {
        this.commentCount = this.commentCount + 1;
    }

    public boolean isProjectLeader(Long leaderId) {
        return Objects.equals(user.getId(), leaderId);
    }
}
