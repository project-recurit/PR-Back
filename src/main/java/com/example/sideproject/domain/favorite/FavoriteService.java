package com.example.sideproject.domain.favorite;

import com.example.sideproject.domain.favorite.listener.FavoriteEvent;
import com.example.sideproject.domain.user.entity.User;
import org.springframework.context.ApplicationEventPublisher;

public abstract class FavoriteService<T extends Favorite> {
    private final ApplicationEventPublisher publisher;

    public FavoriteService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public Long saveFavorite(User user, Long id) {
        T entity = save(user, id);
        publishAddEvent(entity.getTypeId());
        return entity.getTypeId();
    }

    public void deleteFavorite(User user, Long id) {
        Long typeId = delete(user, id);
        publishRemoveEvent(typeId);
    }

    protected abstract T save(User user, Long typeId);
    protected abstract Long delete(User user, Long id);
    public abstract T getFavorite(Long id);

    /**
     * 관심목록 카운트 +1 이벤트 발행
     * @param typeId 카운트가 있는 실 데이터 고유번호
     */
    protected void publishAddEvent(Long typeId) {
        publisher.publishEvent(new FavoriteEvent(typeId, FavoriteMode.ADD, FavoriteDomain.PR));
    }

    /**
     * 관심목록 카운트 -1 이벤트 발행
     * @param typeId 카운트가 있는 실 데이터 고유번호
     */
    protected void publishRemoveEvent(Long typeId) {
        publisher.publishEvent(new FavoriteEvent(typeId, FavoriteMode.REMOVE, FavoriteDomain.PR));
    }
}
