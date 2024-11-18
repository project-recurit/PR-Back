package com.example.sideproject.domain.bookmark.controller;

import com.example.sideproject.domain.bookmark.service.TeamRecruitBookmarkService;
import com.example.sideproject.global.dto.ResponseMessageDto;
import com.example.sideproject.global.enums.ResponseStatus;
import com.example.sideproject.domain.bookmark.dto.TeamRecruitBookmarkPageResponseDto;
import com.example.sideproject.global.security.UserDetailsImpl;
import com.example.sideproject.global.dto.ResponseDataDto;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmarks/team-recruit")
public class TeamRecruitBookmarkController {
    private final TeamRecruitBookmarkService bookmarkService;

    @PostMapping("/{teamRecruitId}")
    public ResponseEntity<ResponseMessageDto> toggleBookmark(
            @PathVariable Long teamRecruitId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        bookmarkService.toggleBookmark(teamRecruitId, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.BOOKMARK_SUCCESS));
    }

     @GetMapping("/my")
    public ResponseEntity<ResponseDataDto<TeamRecruitBookmarkPageResponseDto>> getMyBookmarks(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        TeamRecruitBookmarkPageResponseDto response = 
            bookmarkService.getMyBookmarks(userDetails.getUser(), page, size);
            
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.GET_BOOKMARKS_SUCCESS, response));
    }
} 