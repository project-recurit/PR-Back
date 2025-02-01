package com.example.sideproject.domain.pr.dto.search;

import com.example.sideproject.domain.user.entity.TechStack1;
import com.example.sideproject.global.validation.techstack.TechStackSize;

import java.util.Arrays;
import java.util.List;

public record SearchPublicRelationRequest(
        @TechStackSize String stacks
) {

    public List<TechStack1> toList() {
        String[] stackArr = stacks.split(",");
        return Arrays.stream(stackArr)
                .map(TechStack1::valueOf)
                .toList();
    }
}
