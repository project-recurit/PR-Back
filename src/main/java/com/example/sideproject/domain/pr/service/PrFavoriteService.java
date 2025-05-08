package com.example.sideproject.domain.pr.service;

import com.example.sideproject.domain.favorite.FavoriteService;
import com.example.sideproject.domain.pr.entity.Pr;
import com.example.sideproject.domain.pr.entity.PrFavorite;
import com.example.sideproject.domain.pr.repository.PrFavoriteRepository;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.enums.ErrorType;
import com.example.sideproject.global.exception.CustomException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class PrFavoriteService extends FavoriteService<PrFavorite> {
    private final PrService prService;
    private final PrFavoriteRepository prFavoriteRepository;

    public PrFavoriteService(PrService prService, PrFavoriteRepository prFavoriteRepository, ApplicationEventPublisher publisher) {
        super(publisher);
        this.prService = prService;
        this.prFavoriteRepository = prFavoriteRepository;
    }

    @Override
    protected PrFavorite save(User user, Long typeId) {
        if (prFavoriteRepository.existsByUserAndPr_Id(user, typeId)) {
            throw new CustomException(ErrorType.ALREADY_EXIST_FAVORITE);
        }

        Pr pr = prService.getPr(typeId);
        PrFavorite favorite = PrFavorite.builder()
                .pr(pr)
                .user(user)
                .build();
        return prFavoriteRepository.save(favorite);
    }

    @Override
    protected Long delete(User user, Long id) {
        PrFavorite prFavorite = getFavorite(id);
        if (!prFavorite.isOwn(user.getId())) {
            throw new CustomException(ErrorType.NOT_OWNER);
        }
        Long prId = prFavorite.getPr().getId();
        prFavoriteRepository.delete(prFavorite);
        return prId;
    }

    @Override
    public PrFavorite getFavorite(Long favoriteId) {
        return prFavoriteRepository.findById(favoriteId).orElseThrow(
                () -> new CustomException(ErrorType.PR_FAVORITE_NOT_FOUND)
        );
    }
}
