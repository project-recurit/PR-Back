package com.example.sideproject.domain.favorite;

import com.example.sideproject.domain.user.entity.User;

import java.util.Objects;

public interface Favorite {
    Long getTargetId();
    Long getId();
    User getUser();

    default boolean isOwn(Long userId) {
        return Objects.equals(getUser().getId(), userId);
    }
}
