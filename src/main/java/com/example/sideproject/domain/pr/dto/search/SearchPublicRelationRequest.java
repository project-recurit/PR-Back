package com.example.sideproject.domain.pr.dto.search;

import com.example.sideproject.domain.user.entity.TechStack;
import com.example.sideproject.global.validation.techstack.TechStackSize;

import java.util.Arrays;
import java.util.List;

public record SearchPublicRelationRequest(
        @TechStackSize String stacks
) {

    public List<TechStack> toList() {
        String[] stackArr = stacks.split(",");
        return Arrays.stream(stackArr)
                .map(TechStack::valueOf)
                .toList();
    }
}
