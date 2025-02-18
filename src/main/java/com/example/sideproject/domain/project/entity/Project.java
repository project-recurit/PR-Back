package com.example.sideproject.domain.project.entity;

import com.example.sideproject.domain.chat.entity.ChatRoom;
import com.example.sideproject.domain.user.entity.User;
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

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ProjectUrl> fileUrls = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ProjectTechStack> projectTechStacks = new ArrayList<>();

    @Column(nullable = false, name = "view_count")
    private int viewCount;

    @Column(nullable = false, name = "like_count")
    private int likeCount;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private User user;

    @Column(nullable = false, name = "recruitment_period")
    private String recruitmentPeriod;

    @Column(nullable = false, name = "recruit_status")
    @Enumerated(EnumType.STRING)
    private RecruitStatus recruitStatus;

    @Column(nullable = false, name = "team_size")
    private String teamSize;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChatRoom> chatRooms = new ArrayList<>();

    @Builder
    public Project(String title, String content, List<ProjectTechStack> projectTechStacks,
                   String expectedPeriod, User user,
                   String recruitmentPeriod, RecruitStatus recruitStatus,
                   int viewCount, int likeCount,String teamSize, Long id) {
        this.title = title;
        this.content = content;
        this.expectedPeriod = expectedPeriod;
        this.user = user;
        this.recruitmentPeriod = recruitmentPeriod;
        this.recruitStatus = recruitStatus;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.projectTechStacks = projectTechStacks;
        this.teamSize = teamSize;
        this.id = id;
    }

    public void update(String title, String content,
                       String expectedPeriod) {
        this.title = title;
        this.content = content;
        this.expectedPeriod = expectedPeriod;
    }
}
