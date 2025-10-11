package com.example.sideproject.domain.techstack.dto;

import java.util.List;

public interface TechStackResponse {
    TechStackResponse setTechStacks(List<TechStackDto> techStacks);
    Long getTargetId();
}
