package com.example.sideproject.domain.bookmark.entity;

import com.example.sideproject.global.entity.Timestamped;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.project.entity.Project;
    
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TeamRecruitBookmark extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    public TeamRecruitBookmark(User user, Project project) {
        this.user = user;
        this.project = project;
    }
} 