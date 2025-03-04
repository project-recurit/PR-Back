package com.example.sideproject.domain.applicant.entity;

import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Applicant extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "applicant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uesr_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private String position;

    public void updateStatus(ApplicationStatus status) {
        this.status = status;
    }

    public boolean isOwn(Long applicantId) {
        return Objects.equals(user.getId(), applicantId);
    }
}
