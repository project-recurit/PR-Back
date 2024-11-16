package com.example.sideproject.domain.user.dto;

import com.example.sideproject.domain.user.entity.TechStack;
import lombok.Getter;

import java.util.Set;

@Getter
public class UpdateTechStackRequest {
    private Set<TechStack> techStacks;
}
