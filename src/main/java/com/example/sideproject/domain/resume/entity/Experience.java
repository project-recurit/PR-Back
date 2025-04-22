package com.example.sideproject.domain.resume.entity;

import com.example.sideproject.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "resume_experience")
public class Experience extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_experience_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private int teamSize;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String documentUrl;

    public void addResume(Resume resume) {
        this.resume = resume;
    }

    public void update(Experience experience) {
        this.title = experience.title;
        this.startDate = experience.startDate;
        this.endDate = experience.endDate;
        this.teamSize = experience.teamSize;
        this.description = experience.description;
        this.documentUrl = experience.documentUrl;
    }
}
