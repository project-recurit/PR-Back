package com.example.sideproject.domain.pr.entity;

import com.example.sideproject.domain.techstack.entity.TechStack;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class PrTechStack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pr_id")
    private Pr pr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tech_stack_id")
    private TechStack techStack;

    private int level;

    public void addPr(Pr pr) {
        this.pr = pr;
    }
}
