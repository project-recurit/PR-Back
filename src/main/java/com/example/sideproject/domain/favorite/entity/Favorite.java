package com.example.sideproject.domain.favorite.entity;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Favorite extends Timestamped {
    @Id
    @Column(name = "favorite_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "favorite_item_id")
    private Long item_id;

    @Column(name = "favorite_item_type")
    private FavoriteType item_type;

//    @Builder
//    public Favorite(User user, Project project, Resume resume, FavoriteType type) {
//        this.user = user;
//        this.project = project;
//        this.resume = resume;
//        this.type = type;
//    }

}
