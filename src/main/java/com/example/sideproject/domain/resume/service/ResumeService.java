package com.example.sideproject.domain.resume.service;

import com.example.sideproject.domain.pr.dto.PrResponseDto;
import com.example.sideproject.domain.resume.dto.ResumeListResponseDto;
import com.example.sideproject.domain.resume.dto.ResumeRequestDto;
import com.example.sideproject.domain.resume.dto.ResumeResponseDto;
import com.example.sideproject.domain.resume.entity.Resume;
import com.example.sideproject.domain.resume.repository.ResumeRepository;
import com.example.sideproject.domain.resume.repository.query.ResumeQueryRepository;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final ResumeQueryRepository resumeQueryRepository;

    /**
     * 유저의 이력 리스트 조회
     * @param user 유저
     * @return 이력 리스트
     */
    public List<ResumeListResponseDto> getResumes(User user) {
        return resumeQueryRepository.getResumes(user.getId());
    }

    /**
     * 이력 단건 조회
     * @param user 유저
     * @param resumeId 이력 고유번호
     * @return 해댱 이력의 모든 데이터 조회
     */
    public ResumeResponseDto getResume(User user, Long resumeId) {
        Resume resume = getResumeByIdAndUser(resumeId, user);
        return ResumeResponseDto.of(resume);
    }

    /**
     * 이력 저장
     * @param user 유저
     * @param req 이력 정보
     * @return 저장된 이력 고유번호
     */
    public Long saveResume(User user, ResumeRequestDto req) {
        Resume resume = req.toEntity(user);
        return resumeRepository.save(resume).getId();
    }

    /**
     * 이력 수정
     * @param user 유저
     * @param resumeId 이력 고유번호
     * @param req 수정할 이력 정보
     */
    @Transactional
    public void updateResume(User user, Long resumeId, ResumeRequestDto req) {
        Resume resume = getResumeByIdAndUser(resumeId, user);

        Resume newResume = req.toEntity(user);
        resume.update(newResume);
    }

    /**
     * 이력 삭제
     * @param user 유저
     * @param resumeId 이력 고유번호
     */
    public void deleteResume(User user, Long resumeId) {
        Resume resume = getResumeByIdAndUser(resumeId, user);
        resumeRepository.delete(resume);
    }

    /**
     * 이력서 게시
     * @param user 유저
     * @param resumeId 이력서 고유번호
     */
    @Transactional
    public void publish(User user, Long resumeId) {
        Resume resume = getResumeByIdAndUser(resumeId, user);
        if (!resume.canPublish()) {
            throw new CustomException(ErrorType.PUBLISH_FAILED);
        }
        resume.setPublished(true);
    }

    /**
     * 이력서 게시 취소
     * @param user 유저
     * @param resumeId 이력서 고유 번호
     */
    @Transactional
    public void unPublish(User user, Long resumeId) {
        Resume resume = getResumeByIdAndUser(resumeId, user);
        resume.setPublished(false);
    }

    public Page<PrResponseDto> getPublishedResumes(Pageable pageable) {
        return resumeQueryRepository.getPublishedResumes(pageable);
    }

    public Resume getResumeByUser(User user) {
        return resumeRepository.findResumeByUser(user).orElseThrow(
                () -> new CustomException(ErrorType.RESUME_NOT_FOUND)
        );
    }

    public Resume getResumeByIdAndUser(Long resumeId, User user) {
        return resumeRepository.findResumeByIdAndUser(resumeId, user).orElseThrow(
                () -> new CustomException(ErrorType.RESUME_NOT_FOUND)
        );
    }

    public Resume getResumeById(Long resumeId) {
        return resumeRepository.findResumeById(resumeId).orElseThrow(
                () -> new CustomException(ErrorType.RESUME_NOT_FOUND)
        );
    }
}
