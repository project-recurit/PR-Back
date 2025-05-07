package com.example.sideproject.domain.pr.service;

import com.example.sideproject.domain.pr.dto.*;
import com.example.sideproject.domain.pr.entity.Pr;
import com.example.sideproject.domain.pr.repository.PrRepository;
import com.example.sideproject.domain.pr.repository.query.PrQueryRepository;
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
public class PrService {
    private final PrRepository prRepository;
    private final PrQueryRepository prQueryRepository;

    public Long savePr(User user, PrRequest prRequest) {
        Pr pr = prRequest.toEntity(user);
        return prRepository.save(pr).getId();
    }

    @Transactional
    public PrResponse read(Long prId) {
        Pr pr = prRepository.findPrById(prId).orElseThrow(() -> new CustomException(ErrorType.PR_NOT_FOUND));
        pr.increaseViewCount();
        return new PrResponse(pr);
    }

    public Pr getPr(Long prId) {
        return prRepository.findById(prId).orElseThrow(
                () -> new CustomException(ErrorType.PR_NOT_FOUND)
        );
    }

    @Transactional
    public Long updatePr(User user, Long prId, PrRequest prRequest) {
        Pr pr = getPr(prId);
        if (!pr.isOwner(user.getId())) {
            throw new CustomException(ErrorType.NOT_OWNER);
        }
        pr.update(prRequest);
        return pr.getId();
    }

    public void deletePr(User user, Long prId) {
        Pr pr = getPr(prId);
        if (!pr.isOwner(user.getId())) {
            throw new CustomException(ErrorType.NOT_OWNER);
        }
        prRepository.delete(pr);
    }

    public PagedModel<PrListResponseDto> getPrs(Pageable pageable, PrSort prSort, PrSearchRequest prSearchRequest) {
        if (prSort == null) {
            prSort = PrSort.modifiedAt;
        }
        Sort sort = Sort.by(Sort.Order.desc(prSort.getOrder()));
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<PrListResponseDto> prs = prQueryRepository.getPrs(pageRequest, prSearchRequest);
        return new PagedModel<>(prs);
    }
}
