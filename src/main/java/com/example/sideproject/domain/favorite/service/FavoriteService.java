package com.example.sideproject.domain.favorite.service;

import com.example.sideproject.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    public void saveProjectToFavorites(User user) {
        // NOTE 0227: 관심목록에 이미 저장된 중복데이터가 없는지 확인 -> 예외처리
        // NOTE 0227: 팀원 구인 글을 관심목록에 저장
    }
}
