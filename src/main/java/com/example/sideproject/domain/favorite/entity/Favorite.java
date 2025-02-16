package com.example.sideproject.domain.favorite.entity;

import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.resume.entity.Resume;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Favorite extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    private FavoriteType type;

    @Builder
    public Favorite(User user, Project project, Resume resume, FavoriteType type) {
        this.user = user;
        this.project = project;
        this.resume = resume;
        this.type = type;
    }

}
