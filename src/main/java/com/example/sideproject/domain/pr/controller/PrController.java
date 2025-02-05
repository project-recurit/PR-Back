package com.example.sideproject.domain.pr.controller;

import com.example.sideproject.domain.pr.dto.PrResponseDto;
import com.example.sideproject.domain.pr.service.PrService;
import com.example.sideproject.domain.resume.dto.ResumeResponseDto;
import com.example.sideproject.global.dto.ResponseDataDto;
import com.example.sideproject.global.enums.ResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "pr api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post/pr")
public class PrController {
    private final PrService prService;

    @Operation(summary = "pr 게시글 리스트 조회", description = "페이징 처리된 pr 게시글 리스트 조회")
    @GetMapping
    public ResponseEntity<ResponseDataDto<PagedModel<PrResponseDto>>> getPrs(Pageable pageable) {
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, prService.getPrs(pageable)));
    }

    @Operation(summary = "pr 게시글 조회", description = "해당하는 pr 게시글 상세 조회")
    @GetMapping("/{prId}")
    public ResponseEntity<ResponseDataDto<ResumeResponseDto>> getPr(@PathVariable("prId") Long prId) {
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, prService.getPr(prId)));
    }
}
