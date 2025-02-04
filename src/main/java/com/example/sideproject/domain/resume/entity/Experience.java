package com.example.sideproject.domain.resume.entity;

import com.example.sideproject.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Experience extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "experience_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    private String title;

    private String period;

    private int teamSize;

    @Column(columnDefinition = "TEXT")
    private String achievement;

    private String documentUrl;

    public void addResume(Resume resume) {
        this.resume = resume;
    }

    public void update(Experience experience) {
        this.title = experience.title;
        this.period = experience.period;
        this.teamSize = experience.teamSize;
        this.achievement = experience.achievement;
        this.documentUrl = experience.documentUrl;
    }
}
