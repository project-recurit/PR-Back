package com.example.sideproject.domain.favorite.service;

import com.example.sideproject.domain.favorite.entity.Favorite;
import com.example.sideproject.domain.favorite.entity.FavoriteType;
import com.example.sideproject.domain.favorite.repository.FavoriteRepository;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    public void saveItemToFavorites(Long itemId, User user, FavoriteType itemType) {
        // NOTE 관심목록에 이미 저장된 중복데이터가 없는지 확인 -> 예외처리
        validateDulicateFavorite(itemId, user);

        // NOTE: 팀원 구인 글을 관심목록에 저장
        favoriteRepository.save(Favorite.builder()
                .user(user)
                .itemId(itemId)
                .itemType(itemType)
                .build());
    }

    private void validateDulicateFavorite(Long itemId, User user) {
        if (favoriteRepository.existsByItemIdAndUser(itemId, user)) {
            throw new CustomException(ErrorType.DUPLICATE_FAVORITE);
        }
    }

}
