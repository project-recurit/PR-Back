package com.example.sideproject.domain.user.dto;


import com.example.sideproject.domain.user.entity.UserTechStack;
import lombok.Getter;

import java.util.List;
@Getter
public class UpdateTechStackRequest {
    private List<UserTechStack> userTechStacks;
}
