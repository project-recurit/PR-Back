package com.example.sideproject.domain.pr.controller;

import com.example.sideproject.domain.pr.service.PrFavoriteService;
import com.example.sideproject.global.dto.ResponseDataDto;
import com.example.sideproject.global.enums.ResponseStatus;
import com.example.sideproject.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "pr 관심목록 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorites")
public class PrFavoriteController {
    private final PrFavoriteService prFavoriteService;

    @Operation(summary = "pr 관심 목록 저장", description = "관심 목록에 해당하는 pr 저장")
    @PostMapping("/prs/{prId}")
    public ResponseEntity<ResponseDataDto<Long>> savePrFavorite(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                @PathVariable("prId") Long prId) {
        Long id = prFavoriteService.saveFavorite(userDetails.getUser(), prId);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, id));
    }

    @Operation(summary = "pr 관심 목록 삭제", description = "관심 목록에 해당하는 pr 삭제")
    @DeleteMapping("/{favoriteId}/pr")
    public ResponseEntity<Void> removePrFavorite(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @PathVariable("favoriteId") Long favoriteId) {
        prFavoriteService.deleteFavorite(userDetails.getUser(), favoriteId);
        return ResponseEntity.noContent().build();
    }
}
