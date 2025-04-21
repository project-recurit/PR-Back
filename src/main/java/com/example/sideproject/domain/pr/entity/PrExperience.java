package com.example.sideproject.domain.pr.entity;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class PrExperience extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pr_experience_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pr_id")
    private Pr pr;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private int teamSize;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String documentUrl;

    public void addPr(Pr pr) {
        this.pr = pr;
    }
}
