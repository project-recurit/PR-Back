package com.example.sideproject.domain.pr.listener;

import com.example.sideproject.global.enums.FavoriteMode;

public record PrFavoriteEvent(
        Long prId,
        FavoriteMode mode
) {
}
