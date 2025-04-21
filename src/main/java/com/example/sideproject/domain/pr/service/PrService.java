package com.example.sideproject.domain.pr.service;

import com.example.sideproject.domain.pr.dto.PrListResponse;
import com.example.sideproject.domain.pr.dto.PrRequest;
import com.example.sideproject.domain.pr.entity.Pr;
import com.example.sideproject.domain.pr.repository.PrRepository;
import com.example.sideproject.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PrService {
    private final PrRepository prRepository;

    public Long savePr(User user, PrRequest prRequest) {
        Pr pr = prRequest.toEntity(user);
        return prRepository.save(pr).getId();
    }

}
