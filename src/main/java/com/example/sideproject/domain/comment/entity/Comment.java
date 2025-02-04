package com.example.sideproject.domain.comment.entity;

import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String content;

    private long parentId;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "teamRecruit_id")
    private Project project;

    public Comment(String content, long parentId, User user, Project project) {
        this.content = content;
        this.parentId = parentId;
        this.user = user;
        this.project = project;
    }

}
