package com.example.sideproject.domain.project.entity;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "project")
public class Project extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private User user;
    
    @Column(name = "title", nullable = false)
    @Comment(value = "제목")
    private String title;
    
    @Column(name = "description", nullable = false)
    @Comment(value = "내용")
    private String description;
    
    @Column(name = "project_url", nullable = true)
    @Comment(value = "프로젝트URL")
    private String projectUrl;
    
    @Column(name = "team_count", nullable = false)
    @Comment(value = "참여인원")
    private int teamCount;

    @Column(name = "start_date", nullable = false)
    @Comment(value = "시작날짜")
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = true)
    @Comment(value = "종료날짜")
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectMember> projectMembers = new ArrayList<>();

    @Builder
    public Project(Long id, User user, String title, String description, String projectUrl, int teamCount, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.description = description;
        this.projectUrl = projectUrl;
        this.teamCount = teamCount;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
