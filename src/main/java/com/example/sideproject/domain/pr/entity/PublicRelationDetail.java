package com.example.sideproject.domain.pr.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class PublicRelationDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pr_id")
    private PublicRelation pr;

    private String title;
    private String description;

    public void addPr(PublicRelation pr) {
        this.pr = pr;
    }
}
