package com.example.sideproject.domain.favorite.entity;

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
    @Column(nullable = false, name = "favorite_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false, name = "favorite_item_id")
    private Long itemId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "favorite_item_type")
    private FavoriteType itemType;

    @Builder
    public Favorite(User user, Long itemId, FavoriteType itemType) {
        this.user = user;
        this.itemId = itemId;
        this.itemType = itemType;
    }

}
