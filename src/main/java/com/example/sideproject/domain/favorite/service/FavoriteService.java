package com.example.sideproject.domain.favorite.service;

import com.example.sideproject.domain.favorite.entity.Favorite;
import com.example.sideproject.domain.favorite.entity.FavoriteType;
import com.example.sideproject.domain.favorite.repository.FavoriteRepository;
import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.resume.entity.Resume;
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

    @Transactional
    public void saveProjectToFavorites(Project project, User user) {
        // 중복된 데이터가 있는지 확인 후 예외 처리
        validateDuplicateProjectFavorite(project, user);

        // 관심 목록에 프로젝트 저장
        Favorite favorite = Favorite.builder()
                .user(user)
                .project(project)
                .type(FavoriteType.PROJECT) // Type.PROJECT → FavoriteType.PROJECT로 변경
                .build();

        favoriteRepository.save(favorite);
    }


    private void validateDuplicateProjectFavorite(Project project, User user) {
        if (favoriteRepository.existsByProjectAndUser(project, user)) {
            throw new CustomException(ErrorType.DUPLICATE_FAVORITE);
        }
    }

    @Transactional
    public void saveResumeToFavorites(Resume resume, User user) {
        // 중복된 데이터가 있는지 확인 후 예외 처리
        validateDuplicateResumeFavorite(resume, user);

        // 관심 목록에 이력서 저장
        Favorite favorite = Favorite.builder()
                .user(user)
                .resume(resume)
                .type(FavoriteType.RESUME)
                .build();

        favoriteRepository.save(favorite);
    }


    private void validateDuplicateResumeFavorite(Resume resume, User user) {
        if (favoriteRepository.existsByResumeAndUser(resume, user)) {
            throw new CustomException(ErrorType.DUPLICATE_FAVORITE);
        }
    }

    @Transactional
    public void deleteFavorite(Long favoriteId, User user) {
        // 관심 목록 존재 여부 확인
        Favorite favorite = findFavorite(favoriteId);

        // 본인의 관심 목록인지 확인 후 예외 처리
        if (isNotFavoriteOwner(favorite, user)) {
            throw new CustomException(ErrorType.FAVORITE_ACCESS_DENIED);
        }

        favoriteRepository.delete(favorite);
    }


    private Favorite findFavorite(Long favoriteId) {
        return favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_FAVORITE));
    }

    private boolean isNotFavoriteOwner(Favorite favorite, User user) {
        return !Objects.equals(favorite.getUser().getId(), user.getId());
    }
}
