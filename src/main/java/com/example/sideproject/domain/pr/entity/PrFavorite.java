package com.example.sideproject.domain.pr.entity;

import com.example.sideproject.domain.favorite.Favorite;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class PrFavorite extends Timestamped implements Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pr_favorite_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "pr_id")
    private Pr pr;

    public boolean isOwn(Long userId) {
        return Objects.equals(user.getId(), userId);
    }

    @Override
    public Long getTypeId() {
        return pr.getId();
    }

}
