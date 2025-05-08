package com.example.sideproject.domain.recruitment.controller;

import com.example.sideproject.domain.recruitment.dto.RecruitmentCommentRequestDto;
import com.example.sideproject.domain.recruitment.dto.RecruitmentCommentResponseDto;
import com.example.sideproject.domain.recruitment.entity.RecruitmentFavorite;
import com.example.sideproject.domain.recruitment.service.RecruitmentCommentService;
import com.example.sideproject.domain.recruitment.service.RecruitmentFavoriteService;
import com.example.sideproject.global.dto.ResponseDataDto;
import com.example.sideproject.global.dto.ResponseMessageDto;
import com.example.sideproject.global.enums.ResponseStatus;
import com.example.sideproject.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorites")
@Tag(name = "구인 글 관심목록 api")
public class RecruitmentFavoriteController {

    private final RecruitmentFavoriteService recruitmentFavoriteService;

    @Operation(summary = "구인 글 관심 목록 저장", description = "관심 목록에 해당하는 구인 공고 저장")
    @PostMapping("/recruitments/{recruitmentId}")
    public ResponseEntity<ResponseDataDto<Long>> saveRecruitmentFavorite(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                         @PathVariable("recruitmentId") Long recruitmentId) {
        Long id = recruitmentFavoriteService.saveFavorite(userDetails.getUser(), recruitmentId);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, id));
    }

    @Operation(summary = "구인 글 관심 목록 삭제", description = "관심 목록에 해당하는 구인 공고 삭제")
    @DeleteMapping("/{favoriteId}/recruitment")
    public ResponseEntity<Void> removeRecruitmentFavorite(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @PathVariable("favoriteId") Long favoriteId) {
        recruitmentFavoriteService.deleteFavorite(userDetails.getUser(), favoriteId);
        return ResponseEntity.noContent().build();
    }
}
