package com.example.sideproject.domain.favorite.controller;

import com.example.sideproject.domain.favorite.service.ServiceFacade;
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
    private final ServiceFacade serviceFacade;

    @PostMapping("/projects/{projectId}/favorite")
    public ResponseEntity<ResponseMessageDto> saveFavoritesProject(
            @PathVariable("projectId") Long projectId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        serviceFacade.saveFavoritesProject(projectId, userDetails.getUser());

        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.FAVORITE_CREATE_SUCCESS));
    }

}
