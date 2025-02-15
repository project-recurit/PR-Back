package com.example.sideproject.domain.favorite.service;

import com.example.sideproject.domain.bookmark.entity.TeamRecruitBookmark;
import com.example.sideproject.domain.favorite.entity.Favorite;
import com.example.sideproject.domain.favorite.repository.FavoriteRepository;
import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public void saveProjectToFavorites(Project project, User user) {
        // NOTE 0227: 관심목록에 이미 저장된 중복데이터가 없는지 확인 -> 예외처리
        validateDulicateFavorite(project, user);

        // NOTE 0227: 팀원 구인 글을 관심목록에 저장
        favoriteRepository.save(new Favorite(user, project));
    }

    private void validateDulicateFavorite(Project project, User user) {
        if (favoriteRepository.existsByProjectAndUser(project, user)) {
            throw new CustomException(ErrorType.DUPLICATE_FAVORITE);
        }
    }
}
