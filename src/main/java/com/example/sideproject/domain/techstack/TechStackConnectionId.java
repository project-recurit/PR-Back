package com.example.sideproject.domain.techstack;

import jakarta.persistence.Embeddable;
import jakarta.persistence.IdClass;

import java.io.Serializable;

@Embeddable
public class TechStackConnectionId implements Serializable {
    private Long techStackId;
    private Long targetId;
}
