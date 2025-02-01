package com.example.sideproject.domain.techstack;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class AbstractTechStackConnection<T extends TechStackMappable> {
    @EmbeddedId
    protected TechStackConnectionId techStackId;

    @ManyToOne
    @MapsId("techStackId")
    @JoinColumn(name = "tech_stack_id")
    protected TechStack techStack;

    @ManyToOne
    @MapsId("targetId")
    @JoinColumn(name = "target_id")
    protected T target;
}
