package com.example.sideproject.domain.favorite.controller;

import com.example.sideproject.domain.favorite.service.FavoriteService;
import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.project.service.ProjectService;
import com.example.sideproject.domain.resume.entity.Resume;
import com.example.sideproject.domain.resume.service.ResumeService;
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
    private final FavoriteService favoriteService;
    private final ProjectService projectService;
    private final ResumeService resumeService;

    @PostMapping("/projects/{projectId}/favorite")
    public ResponseEntity<ResponseMessageDto> saveFavoritesProject(
            @PathVariable("projectId") Long projectId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Project project = projectService.validateProject(projectId, userDetails.getUser());
        favoriteService.saveProjectToFavorites(project, userDetails.getUser());

        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.FAVORITE_CREATE_SUCCESS));
    }

    @PostMapping("/resumes/{resumeId}/favorite")
    public ResponseEntity<ResponseMessageDto> saveFavoritesResume(
            @PathVariable("resumeId") Long resumeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Resume resume = resumeService.validateResume(resumeId, userDetails.getUser());
        favoriteService.saveResumeToFavorites(resume, userDetails.getUser());

        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.FAVORITE_CREATE_SUCCESS));
    }

    @DeleteMapping("/favorites/{favoriteId}")
    public ResponseEntity<ResponseMessageDto> deleteFavorite(
            @PathVariable("favoriteId") Long favoriteId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        favoriteService.deleteFavorite(favoriteId, userDetails.getUser());

        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.FAVORITE_DELETE_SUCCESS));
    }

}
