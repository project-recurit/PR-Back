package com.example.sideproject.domain.bookmark.entity;

import com.example.sideproject.domain.recruitment.entity.Recruitment;
import com.example.sideproject.global.entity.Timestamped;
import com.example.sideproject.domain.user.entity.User;

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
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    public TeamRecruitBookmark(User user, Recruitment recruitment) {
        this.user = user;
        this.recruitment = recruitment;
    }
} 