package com.example.sideproject.domain.favorite.service;

import com.example.sideproject.domain.favorite.entity.FavoriteType;
import com.example.sideproject.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteFacade {
    private final FavoriteProjectService favoriteProjectService;
    private final FavoriteService favoriteService;

    @Transactional
    public void saveFavoritesProject(Long projectId, User user) {
        favoriteProjectService.validateProject(projectId, user);
        favoriteService.saveItemToFavorites(projectId, user, FavoriteType.PROJECT);
    }

}
