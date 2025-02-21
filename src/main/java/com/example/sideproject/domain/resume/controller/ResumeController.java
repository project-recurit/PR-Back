package com.example.sideproject.domain.resume.controller;

import com.example.sideproject.domain.resume.dto.ResumeRequestDto;
import com.example.sideproject.domain.resume.dto.ResumeResponseDto;
import com.example.sideproject.domain.resume.service.ResumeService;
import com.example.sideproject.global.dto.ResponseDataDto;
import com.example.sideproject.global.dto.ResponseMessageDto;
import com.example.sideproject.global.enums.ResponseStatus;
import com.example.sideproject.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "이력서 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resume")
public class ResumeController {
    private final ResumeService resumeService;

    @Operation(summary = "이력서 조회", description = "토큰에 해당하는 유저의 이력서 조회, 이력서가 없으면 404에러 발생")
    @GetMapping
    public ResponseEntity<ResponseDataDto<ResumeResponseDto>> getResume(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, resumeService.getResume(userDetails.getUser())));
    }

    @Operation(summary = "이력서 저장", description = "이력서를 저장한다.")
    @PostMapping
    public ResponseEntity<ResponseDataDto<Long>> saveResume(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @RequestBody @Valid ResumeRequestDto req) {
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, resumeService.saveResume(userDetails.getUser(), req)));
    }

    @Operation(summary = "이력서 수정", description = "이력서 id에 맞는 데이터가 없다면 404 에러 발생")
    @PutMapping("/{resumeId}")
    public ResponseEntity<ResponseMessageDto> updateResume(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @PathVariable("resumeId") Long resumeId,
                                                           @RequestBody @Valid ResumeRequestDto req) {
        resumeService.updateResume(userDetails.getUser(), resumeId, req);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.SUCCESS));
    }

    @Operation(summary = "이력서 게시", description = "이력서를 게시한다.")
    @PostMapping("/publish/{resumeId}")
    public ResponseEntity<ResponseMessageDto> publish(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @PathVariable("resumeId") Long resumeId) {
        resumeService.publish(userDetails.getUser(), resumeId);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.SUCCESS));
    }

    @Operation(summary = "이력서 게시 취소", description = "게시한 이력서를 취소한다.")
    @DeleteMapping("/publish/{resumeId}")
    public ResponseEntity<ResponseMessageDto> unPublish(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @PathVariable("resumeId") Long resumeId) {
        resumeService.unPublish(userDetails.getUser(), resumeId);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.SUCCESS));
    }
}
