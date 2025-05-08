package com.example.sideproject.domain.recruitment.entity;

import com.example.sideproject.domain.favorite.Favorite;
import com.example.sideproject.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class RecruitmentFavorite implements Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruitment_favorite_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_id")
    Recruitment recruitment;

    @Override
    public Long getTargetId() {
        return recruitment.getId();
    }
}
