package com.example.sideproject.domain.bookmark.entity;

import com.example.sideproject.global.entity.Timestamped;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.team.entity.TeamRecruit;
    
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
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_recruit_id")
    private TeamRecruit teamRecruit;

    public TeamRecruitBookmark(User user, TeamRecruit teamRecruit) {
        this.user = user;
        this.teamRecruit = teamRecruit;
    }
} 