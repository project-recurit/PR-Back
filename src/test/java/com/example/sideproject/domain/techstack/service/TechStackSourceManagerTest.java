package com.example.sideproject.domain.techstack.service;

import com.example.sideproject.domain.techstack.dto.TechStackDto;
import com.example.sideproject.domain.techstack.entity.TechStack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TechStackSourceManagerTest {

    @Test
    @DisplayName("캐시에 데이터가 있으면 캐시에서 반환")
    void should_ReturnFromCache_When_CacheHasData() {
        // given
        CacheTechStackStorage cacheStorage = new FakeCacheStorage(
                List.of(new TechStack(1L, "Java"))
        );
        FakePersistentStorage persistentStorage = new FakePersistentStorage(List.of());

        TechStackSourceManager manager = new TechStackSourceManager(cacheStorage, persistentStorage);

        // when
        List<TechStackDto> result = manager.getTechStackList();

        // then
        assertThat(result).hasSize(1);
        assertThat(persistentStorage.isCalled()).isEqualTo(false);
    }

    @Test
    @DisplayName("캐시가 비어있으면 영구 저장소에서 조회하고 캐시에 저장")
    void should_FetchFromPersistentAndSaveToCache_When_CacheIsEmpty() {
        // given
        FakeCacheStorage cacheStorage = new FakeCacheStorage(List.of());
        FakePersistentStorage persistentStorage = new FakePersistentStorage(
                List.of(new TechStack(1L, "Python"))
        );

        TechStackSourceManager manager = new TechStackSourceManager(cacheStorage, persistentStorage);

        // when
        List<TechStackDto> result = manager.getTechStackList();

        // then
        assertThat(result).hasSize(1);
        assertThat(persistentStorage.isCalled()).isEqualTo(true);
        assertThat(cacheStorage.getSavedData()).hasSize(1);
    }

    // Fake 구현체들
    static class FakeCacheStorage implements CacheTechStackStorage {
        private List<TechStack> data;

        FakeCacheStorage(List<TechStack> data) {
            this.data = new ArrayList<>(data);
        }

        @Override
        public List<TechStack> findAll() {
            return data;
        }

        @Override
        public void saveAll(List<TechStack> techStacks) {
            this.data = new ArrayList<>(techStacks);
        }

        public List<TechStack> getSavedData() {
            return data;
        }
    }

    static class FakePersistentStorage implements PersistentTechStackStorage {
        private final List<TechStack> data;
        private boolean isCalled;

        FakePersistentStorage(List<TechStack> data) {
            this.data = data;
        }

        @Override
        public List<TechStack> findAll() {
            this.isCalled = true;
            return data;
        }

        @Override
        public void saveAll(List<TechStack> techStacks) {}

        public boolean isCalled() {
            return isCalled;
        }
    }
}