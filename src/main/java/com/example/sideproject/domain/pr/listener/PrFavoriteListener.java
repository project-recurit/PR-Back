package com.example.sideproject.domain.pr.listener;

import com.example.sideproject.domain.pr.service.PrService;
import com.example.sideproject.global.enums.FavoriteMode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrFavoriteListener {
    private final PrService prService;

    @EventListener
    public void updateFavoriteCount(PrFavoriteEvent event) {
        Long id = event.prId();
        FavoriteMode mode = event.mode();
        switch (mode) {
            case ADD -> prService.increaseFavoriteCount(id);
            case REMOVE -> prService.decrementFavoriteCount(id);
        }
    }

}
