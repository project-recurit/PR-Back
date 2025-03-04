package com.example.sideproject.domain.pr.controller;

import com.example.sideproject.domain.pr.dto.PublicResumesListResponseDto;
import com.example.sideproject.domain.pr.service.PublicResumesService;
import com.example.sideproject.domain.resume.dto.ResumeResponseDto;
import com.example.sideproject.domain.resume.service.ResumeService;
import com.example.sideproject.global.dto.ResponseDataDto;
import com.example.sideproject.global.enums.ResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class PublicResumesController {
    private final PublicResumesService publicResumesService;
    private final ResumeService resumeService;

    @Operation(summary = "pr 게시글 리스트 조회", description = "페이징 처리된 pr 게시글 리스트 조회")
    @GetMapping
    public ResponseEntity<ResponseDataDto<PagedModel<PublicResumesListResponseDto>>> getPublicResumes(Pageable pageable, Sort sort) {
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, publicResumesService.getPublicResumes(pageable)));
    }

    @Operation(summary = "pr 게시글 조회", description = "해당하는 pr 게시글 상세 조회")
    @GetMapping("/{publicResumeId}")
    public ResponseEntity<ResponseDataDto<ResumeResponseDto>> getPr(@PathVariable("publicResumeId") Long publicResumeId) {
        Long resumeId = publicResumesService.read(publicResumeId);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, resumeService.getResume(resumeId)));
    }

}
