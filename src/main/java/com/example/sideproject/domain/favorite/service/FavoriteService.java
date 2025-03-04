package com.example.sideproject.domain.favorite.service;

import com.example.sideproject.domain.favorite.dto.FavoriteProjectResponseDto;
import com.example.sideproject.domain.favorite.entity.Favorite;
import com.example.sideproject.domain.favorite.entity.FavoriteType;
import com.example.sideproject.domain.favorite.repository.FavoriteRepository;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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

    public void deleteFavorite(Long favoriteId, User user) {
        // NOTE: 관심목록 존재여부 확인 -> 예외처리
        Favorite favorite = findFavorite(favoriteId);

        // NOTE: 자신의 관심목록인지 확인 -> 예외처리
        if(NotFavoriteOwner(favorite, user))
            throw new CustomException(ErrorType.FAVORITE_ACCESS_DENIED);

        favoriteRepository.delete(favorite);
    }

    private Favorite findFavorite(Long favoriteId) {
        return favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_FAVORITE));
    }

    private boolean NotFavoriteOwner(Favorite favorite, User user) {
        return !Objects.equals(favorite.getUser().getId(), user.getId());
    }

    public void readFavoritesProjects(User user) {
    }


    //    private final Long id;
//    private final Long projectId;
//    private final String position;
//    private final List<ProjectTechStack> techStack;
//    private final WorkType workType;
//    private final String recruitmentPeriod;
//    private final String deadline;
//    private final Long viewCount;
//    private final Long commentCount; // NOTE: null
//    private final Long likeCount;

    // SELECT f.id, p.id,
    // FROM Favorite as f
    //  INNER JOIN Project as p
    //  INNER JOIN Position as po
    //  ON f.favorite_item_id = p.id
    //  ON po.project_id = p.id
    //WHERE f.user_id = {user 변수}

}
