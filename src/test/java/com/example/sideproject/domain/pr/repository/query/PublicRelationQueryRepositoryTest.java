package com.example.sideproject.domain.pr.repository.query;

import com.example.sideproject.domain.pr.dto.read.PublicRelationListResponseDto;
import com.example.sideproject.domain.pr.dto.search.SearchPublicRelationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PublicRelationQueryRepositoryTest {

    @Autowired
    PublicRelationQueryRepository publicRelationQueryRepository;

    @Test
    @Transactional
    void findPublicRelationList() {
        String teckStacks = "JAVA,SPRING";
        SearchPublicRelationRequest request = new SearchPublicRelationRequest(teckStacks);
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<PublicRelationListResponseDto> publicRelationList = publicRelationQueryRepository.findPublicRelationList(request, pageRequest);
        System.out.println(publicRelationList.getContent());
    }
}