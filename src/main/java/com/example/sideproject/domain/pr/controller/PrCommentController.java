package com.example.sideproject.domain.pr.controller;

import com.example.sideproject.domain.pr.dto.PrCommentResponse;
import com.example.sideproject.domain.pr.dto.PrCommentRequest;
import com.example.sideproject.domain.pr.service.PrCommentService;
import com.example.sideproject.global.dto.ResponseDataDto;
import com.example.sideproject.global.dto.ResponseMessageDto;
import com.example.sideproject.global.enums.ResponseStatus;
import com.example.sideproject.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "pr 댓글 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/prs")
public class PrCommentController {
    private final PrCommentService prCommentService;

    @Operation(summary = "pr 게시글 댓글 작성", description = "pr 게시글 댓글 작성")
    @PostMapping("/{prId}/comment")
    public ResponseEntity<ResponseDataDto<Long>> saveComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                             @PathVariable("prId") Long prId,
                                                             @RequestBody PrCommentRequest prCommentRequest) {
        Long res = prCommentService.saveComment(userDetails.getUser(), prId, prCommentRequest);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, res));
    }

    @Operation(summary = "pr 게시글 댓글 조회 ", description = "pr 게시글 댓글 조회")
    @GetMapping("/{prId}/comments")
    public ResponseEntity<ResponseDataDto<PagedModel<PrCommentResponse>>> getComments(@PathVariable("prId") Long prId,
                                                                                      Pageable page) {
        PagedModel<PrCommentResponse> res = prCommentService.getComments(prId, page);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, res));
    }

    @Operation(summary = "pr 대댓글 조회", description = "pr 게시글 댓글 조회, 페이징 처리 X")
    @GetMapping("/comment/{parentId}/reply")
    public ResponseEntity<ResponseDataDto<List<PrCommentResponse>>> getReply(@PathVariable("parentId") Long parentId,
                                                                             Pageable page) {
        List<PrCommentResponse> res = prCommentService.getReplys(parentId, page);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, res));
    }

    @Operation(summary = "pr 게시글 댓글 수정", description = "pr 게시글 댓글 작성")
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<ResponseDataDto<Long>> updateComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @PathVariable("commentId") Long commentId,
                                                               @RequestBody PrCommentRequest prCommentRequest) {
        Long res = prCommentService.updateComment(userDetails.getUser(), commentId, prCommentRequest);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, res));
    }

    @Operation(summary = "pr 게시글 댓글 삭제", description = "pr 게시글 댓글 삭제")
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<ResponseMessageDto> deletetComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                             @PathVariable("commentId") Long commentId) {
        prCommentService.deleteComment(userDetails.getUser(), commentId);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.SUCCESS));
    }
}
