package com.example.sideproject.domain.techstack.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class TechStack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tech_stack_id")
    private Long id;

    private String name;

    public String getName() {
        if (name.isEmpty()) {
            return "";
        }
        return name;
    }
}
