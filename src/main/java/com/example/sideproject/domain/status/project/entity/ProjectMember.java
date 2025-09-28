package com.example.sideproject.domain.status.project.entity;

import com.example.sideproject.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Table(name = "project_member")
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public ProjectMember() {}

    @Builder
    public ProjectMember(Long id, User user, Project project) {
        this.id = id;
        this.user = user;
        this.project = project;
    }
}
