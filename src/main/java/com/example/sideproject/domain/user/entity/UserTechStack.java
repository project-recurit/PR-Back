package com.example.sideproject.domain.user.entity;

import com.example.sideproject.domain.techstack.entity.TechStack;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserTechStack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tech_stack_id", nullable = false)
    private TechStack techStack;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private User user;

    public void setUser(User user) {
        this.user = user;
    }
}
