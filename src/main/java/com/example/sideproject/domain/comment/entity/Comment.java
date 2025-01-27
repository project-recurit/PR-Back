package com.example.sideproject.domain.comment.entity;

import com.example.sideproject.domain.team.entity.TeamRecruit;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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
    private TeamRecruit teamRecruit;

    public Comment(String content, long parentId, User user, TeamRecruit teamRecruit) {
        this.content = content;
        this.parentId = parentId;
        this.user = user;
        this.teamRecruit = teamRecruit;
    }

}
