package com.example.sideproject.domain.pr.service;

import com.example.sideproject.domain.pr.entity.Pr;
import com.example.sideproject.domain.pr.entity.PrFavorite;
import com.example.sideproject.domain.pr.listener.PrFavoriteEvent;
import com.example.sideproject.domain.pr.repository.PrFavoriteRepository;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.enums.FavoriteMode;
import com.example.sideproject.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrFavoriteService {
    private final PrService prService;
    private final PrFavoriteRepository prFavoriteRepository;
    private final ApplicationEventPublisher publisher;

    public Long savePrFavorite(User user, Long prId) {
        if (prFavoriteRepository.existsByUserAndPr_Id(user, prId)) {
            throw new CustomException(ErrorType.ALREADY_EXIST_FAVORITE);
        }

        Pr pr = prService.getPr(prId);
        PrFavorite favorite = PrFavorite.builder()
                .pr(pr)
                .user(user)
                .build();
        Long id = prFavoriteRepository.save(favorite).getId();

        PrFavoriteEvent prFavoriteEvent = new PrFavoriteEvent(prId, FavoriteMode.ADD);
        publisher.publishEvent(prFavoriteEvent);
        return id;
    }

    public void removePrFavorite(User user, Long favoriteId) {
        PrFavorite prFavorite = getPrFavorite(favoriteId);
        if (!prFavorite.isOwn(user.getId())) {
            throw new CustomException(ErrorType.NOT_OWNER);
        }
        prFavoriteRepository.delete(prFavorite);

        PrFavoriteEvent prFavoriteEvent = new PrFavoriteEvent(prFavorite.getPr().getId(), FavoriteMode.REMOVE);
        publisher.publishEvent(prFavoriteEvent);

    }

    private PrFavorite getPrFavorite(Long favoriteId) {
        return prFavoriteRepository.findById(favoriteId).orElseThrow(
                () -> new CustomException(ErrorType.PR_FAVORITE_NOT_FOUND)
        );
    }
}
