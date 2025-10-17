package com.example.sideproject.domain.applicant.entity;

import com.example.sideproject.global.enums.Position;
import com.example.sideproject.domain.recruitment.entity.Recruitment;
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
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uesr_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private Position position;

    public void updateStatus(ApplicationStatus status) {
        this.status = status;
    }

    public boolean isOwn(Long inputId) {
        return Objects.equals(user.getId(), inputId);
    }

    public boolean canReadResume(Long inputId) {
        // 지원자 본인인 경우 조회 가능
        if (isOwn(inputId)) {
            return true;
        }

        // 모집글 작성인 경우 조회 가능
        User recruitmentWriter = recruitment.getUser();

        if (recruitmentWriter == null) {
            return false;
        }

        Long id = recruitmentWriter.getId();

        return Objects.equals(inputId, id);
    }
}
