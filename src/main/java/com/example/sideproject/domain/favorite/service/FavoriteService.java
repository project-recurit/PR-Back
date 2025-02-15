package com.example.sideproject.domain.favorite.service;

import com.example.sideproject.domain.bookmark.entity.TeamRecruitBookmark;
import com.example.sideproject.domain.favorite.entity.Favorite;
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
        // NOTE 0207: 관심목록에 이미 저장된 중복데이터가 없는지 확인 -> 예외처리
        validateDulicateProjectFavorite(project, user);

        // NOTE 0207: 팀원 구인 글을 관심목록에 저장
        favoriteRepository.save(Favorite.builder()
                .user(user)
                .project(project)
                .build());
    }

    private void validateDulicateProjectFavorite(Project project, User user) {
        if (favoriteRepository.existsByProjectAndUser(project, user)) {
            throw new CustomException(ErrorType.DUPLICATE_FAVORITE);
        }
    }

    @Transactional
    public void saveResumeToFavorites(Resume resume, User user) {
        // NOTE 0215: 관심목록에 이미 저장된 중복데이터가 없는지 확인 -> 예외처리
        validateDulicateResumeFavorite(resume, user);

        // NOTE 0215: 공개 이력서 관심목록에 저장
        favoriteRepository.save(Favorite.builder()
                .user(user)
                .resume(resume)
                .build());
    }

    private void validateDulicateResumeFavorite(Resume resume, User user) {
        if (favoriteRepository.existsByResumeAndUser(resume, user)) {
            throw new CustomException(ErrorType.DUPLICATE_FAVORITE);
        }
    }

    @Transactional
    public void deleteFavorite(Long favoriteId, User user) {
        // NOTE 0215: 관심목록 존재여부 확인 -> 예외처리
        Favorite favorite = findFavorite(favoriteId);

        // NOTE 0215: 자신의 관심목록인지 확인 -> 예외처리
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
}
