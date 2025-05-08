package com.example.sideproject.domain.favorite.listener;

import com.example.sideproject.domain.favorite.FavoriteDomain;
import com.example.sideproject.domain.favorite.FavoriteMode;

public record FavoriteEvent(
        Long prId,
        FavoriteMode mode,
        FavoriteDomain domain
) {
}
