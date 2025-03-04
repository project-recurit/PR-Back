package com.example.sideproject.domain.favorite.service;

import com.example.sideproject.domain.favorite.entity.FavoriteType;
import com.example.sideproject.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteFacade {
    private final FavoriteService favoriteService;
    private final FavoriteProjectService favoriteProjectService;
    private final FavoriteResumeService favoriteResumeService;

    @Transactional
    public void saveFavoritesProject(Long projectId, User user) {
        favoriteProjectService.validateProject(projectId, user);
        favoriteService.saveItemToFavorites(projectId, user, FavoriteType.PROJECT);
    }

    @Transactional
    public void saveFavoritesResume(Long resumeId, User user) {
        favoriteResumeService.validateResume(resumeId, user);
        favoriteService.saveItemToFavorites(resumeId, user, FavoriteType.RESUME);
    }

    @Transactional
    public void deleteFavorite(Long favoriteId, User user) {
        favoriteService.deleteFavorite(favoriteId, user);
    }


    @Transactional
    public void readFavoritesProjects(User user) {
        favoriteService.readFavoritesProjects(user);
    }
}
