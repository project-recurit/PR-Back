package com.example.sideproject.domain.project.entity;

import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.entity.UserTechStack;
import com.example.sideproject.global.entity.Timestamped;
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

    @Column(nullable = true, name = "expected_period")
    private String expectedPeriod;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectUrl> fileUrls = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectTechStack> projectTechStacks = new ArrayList<>();

    @Column(nullable = false, name = "view_count")
    private int viewCount;

    private String contact;

    @Column(nullable = false, name = "comment_count")
    private int commentCount;

    @Column(nullable = false, name = "favorite_count")
    private int favoriteCount;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private User user;

    @Column(nullable = false, name = "recruitment_period")
    private String recruitmentPeriod;

    @Column(nullable = false, name = "recruit_status")
    @Enumerated(EnumType.STRING)
    private RecruitStatus recruitStatus;

    @Column(nullable = false, name = "team_size")
    private int teamSize;

    @Builder
    public Project(String title, String content, List<ProjectTechStack> projectTechStacks, List<ProjectUrl> projectUrls,
                   String expectedPeriod, String contact, User user,
                   String recruitmentPeriod, RecruitStatus recruitStatus,
                   int viewCount, int commentCount, int favoriteCount, int teamSize, Long id) {
        this.title = title;
        this.content = content;
        this.expectedPeriod = expectedPeriod;
        this.contact = contact;
        this.user = user;
        this.recruitmentPeriod = recruitmentPeriod;
        this.recruitStatus = recruitStatus;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.favoriteCount = favoriteCount;
        this.projectTechStacks = projectTechStacks;
        this.teamSize = teamSize;
        this.id = id;
        this.fileUrls = projectUrls;
    }
}
