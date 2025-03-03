package com.example.sideproject.domain.favorite.service;

import com.example.sideproject.domain.resume.repository.ResumeRepository;
import com.example.sideproject.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteResumeService {
    private final ResumeRepository resumeRepository;

    public void validateResume(Long resumeId, User user) {
        // TODO: 이력서 존재 여부 확인
        // Resume resume = getResumeById(resumeId);

        // NOTE: 이력서 공개 여부 확인
//        if (resume.getPublishedAt() == null)
//            throw new CustomException(ErrorType.NOT_PUBLISH_RESUME);

        // TODO: 자신의 이력서는 관심목록 추가 제외
//        if (resume.getUser().equals(user))
//            throw new CustomException(ErrorType.NOT_MODIFY_OWN);
    }

}
