package com.example.sideproject.domain.favorite.controller;

import com.example.sideproject.domain.favorite.service.FavoriteFacade;
import com.example.sideproject.global.dto.ResponseDataDto;
import com.example.sideproject.global.dto.ResponseMessageDto;
import com.example.sideproject.global.enums.ResponseStatus;
import com.example.sideproject.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FavoriteController {
    private final FavoriteFacade favoriteFacade;

    @PostMapping("/projects/{projectId}/favorite")
    public ResponseEntity<ResponseMessageDto> saveFavoritesProject(
            @PathVariable("projectId") Long projectId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        favoriteFacade.saveFavoritesProject(projectId, userDetails.getUser());

        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.FAVORITE_CREATE_SUCCESS));
    }

    @PostMapping("/resumes/{resumeId}/favorite")
    public ResponseEntity<ResponseMessageDto> saveFavoritesResume(
            @PathVariable("resumeId") Long resumeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        favoriteFacade.saveFavoritesResume(resumeId, userDetails.getUser());

        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.FAVORITE_CREATE_SUCCESS));
    }

    @DeleteMapping("/favorites/{favoriteId}")
    public ResponseEntity<ResponseMessageDto> deleteFavorite(
            @PathVariable("favoriteId") Long favoriteId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        favoriteFacade.deleteFavorite(favoriteId, userDetails.getUser());

        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.FAVORITE_DELETE_SUCCESS));
    }

    @GetMapping("/favorites/projects")
    public ResponseEntity<ResponseDataDto> readFavoritesProjects(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        favoriteFacade.readFavoritesProjects(userDetails.getUser());

        return ResponseEntity.ok(new ResponseDataDto(ResponseStatus.FAVORITE_CREATE_SUCCESS, ""));
    }

}
