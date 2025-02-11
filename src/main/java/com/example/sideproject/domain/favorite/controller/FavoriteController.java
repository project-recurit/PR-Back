package com.example.sideproject.domain.favorite.controller;

import com.example.sideproject.domain.bookmark.service.TeamRecruitBookmarkService;
import com.example.sideproject.domain.favorite.service.FavoriteService;
import com.example.sideproject.domain.project.service.ProjectService;
import com.example.sideproject.global.dto.ResponseMessageDto;
import com.example.sideproject.global.enums.ResponseStatus;
import com.example.sideproject.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final ProjectService projectService;

    @PostMapping("/projects/{projectId}/favorite")
    public ResponseEntity<ResponseMessageDto> saveFavoritesProject( // TODO: 로직 작성
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        projectService.validateProject(projectId, userDetails.getUser());
        favoriteService.saveProjectToFavorites(userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.FAVORITES_CREATE_SUCCESS));
    }
}
