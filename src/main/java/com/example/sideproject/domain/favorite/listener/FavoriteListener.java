package com.example.sideproject.domain.favorite.listener;

import com.example.sideproject.domain.favorite.FavoriteDomain;
import com.example.sideproject.domain.pr.service.PrService;
import com.example.sideproject.domain.recruitment.service.RecruitmentService;
import com.example.sideproject.domain.favorite.FavoriteMode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FavoriteListener {
    private final PrService prService;
    private final RecruitmentService recruitmentService;

    @EventListener
    @Async
    public void updateFavoriteCount(FavoriteEvent event) {
        FavoriteDomain domain = event.domain();
        Long id = event.prId();
        FavoriteMode mode = event.mode();
        switch (domain) {
            case PR -> updatePr(id, mode);
            case RECRUITMENT -> updateRecruitment(id, mode);
        }
    }

    private void updatePr(Long id, FavoriteMode mode) {
        switch (mode) {
            case ADD -> prService.increaseFavoriteCount(id);
            case REMOVE -> prService.decrementFavoriteCount(id);
        }
    }

    private void updateRecruitment(Long id, FavoriteMode mode) {
        // todo count 증가해주는 함수 연동 필요
        switch (mode) {
            case ADD -> {}
            case REMOVE -> {}
        }
    }

}
