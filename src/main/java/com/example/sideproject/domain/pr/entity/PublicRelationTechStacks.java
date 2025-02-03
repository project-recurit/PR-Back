package com.example.sideproject.domain.pr.entity;

import com.example.sideproject.domain.user.entity.TechStack1;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class PublicRelationTechStacks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publicRelation_id")
    private PublicRelation publicRelation;

    private TechStack1 techStack1;

    public void addPublicRelation(PublicRelation publicRelation) {
        this.publicRelation = publicRelation;
    }

    public Long getPublicRelationId() {
        return publicRelation.getId();
    }
}
