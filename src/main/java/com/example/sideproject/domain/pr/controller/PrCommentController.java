package com.example.sideproject.domain.pr.controller;

import com.example.sideproject.domain.pr.dto.PrCommentRequest;
import com.example.sideproject.domain.pr.dto.PrRequest;
import com.example.sideproject.domain.pr.service.PrCommentService;
import com.example.sideproject.global.dto.ResponseDataDto;
import com.example.sideproject.global.enums.ResponseStatus;
import com.example.sideproject.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/prs/{prId}/comments")
public class PrCommentController {
    private final PrCommentService prCommentService;

    @Operation(summary = "pr 게시글 작성", description = "pr 게시글 작성")
    @PostMapping
    public ResponseEntity<ResponseDataDto<Long>> saveComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                             @PathVariable("prId") Long prId,
                                                             @RequestBody PrCommentRequest prCommentRequest) {
        Long res = prCommentService.saveComment(userDetails.getUser(), prCommentRequest);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, res));
    }
}
