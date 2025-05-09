package com.example.sideproject.domain.recruitment.controller;

import com.example.sideproject.domain.recruitment.dto.RecruitmentCommentRequestDto;
import com.example.sideproject.domain.recruitment.dto.RecruitmentCommentResponseDto;
import com.example.sideproject.domain.recruitment.service.RecruitmentCommentService;
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
@RequestMapping("/api/v1")
@Tag(name = "구인 글 댓글 api")
public class RecruitmentCommentController {

    private final RecruitmentCommentService commentService;

    @Operation(summary = "구인 글 댓글 작성", description = "대댓글 작성시 parentId 항목에 부모댓글 Id 기입")
    @PostMapping("/recruitment/{recruitmentId}/comment")
    public ResponseEntity<ResponseMessageDto> createComment(@PathVariable("recruitmentId") Long recruitmentId,
                                                            @RequestBody @Valid RecruitmentCommentRequestDto requestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.createComment(recruitmentId, userDetails.getUser(), requestDto);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.CREATE_SUCCESS_COMMENT));
    }

    @Operation(summary = "구인 글 댓글 조회", description = "사이즈 20개 제한")
    @GetMapping("/recruitment/{recruitmentId}/comments")
    public ResponseEntity<ResponseDataDto<Page<RecruitmentCommentResponseDto>>> getComments(@PathVariable("recruitmentId") Long recruitmentId,
                                                                                 @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<RecruitmentCommentResponseDto> comments = commentService.getComments(recruitmentId, page);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.READ_SUCCESS_COMMENT, comments));
    }

    @Operation(summary = "구인 글 대댓글 조회", description = "parentId 없을 시 404에러 반환")
    @GetMapping("/recruitment/comment/{parentId}/reply")
    public ResponseEntity<ResponseDataDto<List<RecruitmentCommentResponseDto>>> getReply(@PathVariable("parentId") Long parentId) {
        List<RecruitmentCommentResponseDto> reply = commentService.getReply(parentId);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.READ_SUCCESS_COMMENT, reply));
    }

    @Operation(summary = "구인 글 댓글 수정", description = "없는 id 값 넣으면 404, 다른 유저가 수정하면 406에러")
    @PatchMapping("/recruitment/comment/{commentId}")
    public ResponseEntity<ResponseMessageDto> updateComment(@PathVariable("commentId") Long commentId,
                                                            @RequestBody @Valid RecruitmentCommentRequestDto requestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.updateComment(commentId, userDetails.getUser(), requestDto);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.UPDATE_SUCCESS_COMMENT));
    }

    @Operation(summary = "구인 글 댓글 삭제", description = "없는 id 값 넣으면 404, 다른 유저가 수정하면 406에러")
    @DeleteMapping("/recruitment/comment/{commentId}")
    public ResponseEntity<ResponseMessageDto> deleteComment(@PathVariable("commentId") Long commentId,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(commentId, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.DELETE_SUCCESS_COMMENT));
    }
}
