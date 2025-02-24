package com.example.sideproject.domain.pr.service;

import com.example.sideproject.domain.pr.dto.PublicResumesListResponseDto;
import com.example.sideproject.domain.pr.entity.PublicResumes;
import com.example.sideproject.domain.pr.repository.PublicResumesRepository;
import com.example.sideproject.domain.pr.repository.query.PublicResumesQueryRepository;
import com.example.sideproject.domain.resume.entity.Resume;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PublicResumesService {
    private final PublicResumesRepository publicResumesRepository;
    private final PublicResumesQueryRepository publicResumesQueryRepository;

    public PagedModel<PublicResumesListResponseDto> getPublicResumes(Pageable pageable) {
        Sort sort = Sort.by(Sort.Order.desc("publishedAt"));
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<PublicResumesListResponseDto> publishedResumes = publicResumesQueryRepository.getPublicResumes(pageRequest, sort);
        return new PagedModel<>(publishedResumes);
    }

    @Transactional
    public Long read(Long publicResumeId) {
        PublicResumes publicResume = publicResumesRepository.findById(publicResumeId).orElseThrow(
                () -> new CustomException(ErrorType.PUBLIC_RESUME_NOT_FOUND)
        );

        publicResume.increaseViewCount();

        return publicResume.getResume().getId();
    }

    /**
     * 이력 게시 (pr 추가)
     * @param user 유저
     * @param resumeId 이력 고유번호
     */
    public void publish(User user, Long resumeId) {
        Resume resume = new Resume(resumeId);
        if (publicResumesRepository.existsByResume(resume)) {
            throw new CustomException(ErrorType.DUPLICATE_PUBLIC_RESUME);
        }

        PublicResumes publicResumes = PublicResumes.builder()
                .user(user)
                .resume(resume)
                .commentCount(0)
                .favoriteCount(0)
                .viewCount(0)
                .build();
        publicResumesRepository.save(publicResumes);
    }

    /**
     * 이력 게시 취소 (pr 삭제)
     * @param user 유저
     * @param resumeId 이력 고유번호
     */
    public void unPublish(User user, Long resumeId) {
        Resume resume = new Resume(resumeId);
        PublicResumes publicResumes = publicResumesRepository.findByResumeAndUser(resume, user).orElseThrow(
                () -> new CustomException(ErrorType.UNPUBLISHED_RESUME)
        );
        publicResumesRepository.delete(publicResumes);
    }
}
