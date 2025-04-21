package com.example.sideproject.domain.pr.service;

import com.example.sideproject.domain.pr.dto.PrRequest;
import com.example.sideproject.domain.pr.dto.PrResponse;
import com.example.sideproject.domain.pr.entity.Pr;
import com.example.sideproject.domain.pr.repository.PrRepository;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PrService {
    private final PrRepository prRepository;
//    private final PrQueryRepository prQueryRepository;

    public Long savePr(User user, PrRequest prRequest) {
        Pr pr = prRequest.toEntity(user);
        return prRepository.save(pr).getId();
    }

    @Transactional
    public PrResponse read(Long prId) {
        Pr pr = prRepository.findPrById(prId).orElseThrow(() -> new CustomException(ErrorType.PUBLIC_RESUME_NOT_FOUND));
        pr.increaseViewCount();
        return new PrResponse(pr);
    }

    public Pr getPr(Long prId) {
        return prRepository.findById(prId).orElseThrow(
                () -> new CustomException(ErrorType.PUBLIC_RESUME_NOT_FOUND)
        );
    }
}
