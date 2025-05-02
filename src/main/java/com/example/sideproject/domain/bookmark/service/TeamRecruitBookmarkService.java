package com.example.sideproject.domain.bookmark.service;

import com.example.sideproject.domain.bookmark.dto.TeamRecruitBookmarkPageResponseDto;
import com.example.sideproject.domain.bookmark.entity.TeamRecruitBookmark;
import com.example.sideproject.domain.bookmark.repository.TeamRecruitBookmarkRepository;
import com.example.sideproject.domain.recruitment.entity.Recruitment;
import com.example.sideproject.domain.recruitment.repository.RecruitmentRepository;
import com.example.sideproject.domain.user.repository.UserRepository;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.exception.CustomException;
import com.example.sideproject.global.enums.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamRecruitBookmarkService {
    private final TeamRecruitBookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final RecruitmentRepository recruitmentRepository;

    public void toggleBookmark(Long teamRecruitId, User user) {
        User foundUser = userRepository.findById(user.getId())
            .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));

        Recruitment recruitment = recruitmentRepository.findById(teamRecruitId)
            .orElseThrow(() -> new CustomException(ErrorType.TEAM_RECRUIT_NOT_FOUND));

        if (bookmarkRepository.existsByUserAndRecruitment(foundUser, recruitment)) {
            bookmarkRepository.deleteByUserAndRecruitment(foundUser, recruitment);
        } else {
            bookmarkRepository.save(new TeamRecruitBookmark(foundUser, recruitment));
        }
    }

    public TeamRecruitBookmarkPageResponseDto getMyBookmarks(User user, int page, int size) {
        User foundUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<TeamRecruitBookmark> bookmarkPage = bookmarkRepository.findAllByUserOrderByCreatedAtDesc(foundUser, pageable);
        
        return new TeamRecruitBookmarkPageResponseDto(bookmarkPage);
    }
} 