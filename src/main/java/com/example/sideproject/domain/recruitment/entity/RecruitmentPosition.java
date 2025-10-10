package com.example.sideproject.domain.recruitment.entity;

import com.example.sideproject.global.entity.Timestamped;
import com.example.sideproject.global.enums.Position;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "recruitment_position")
public class RecruitmentPosition extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "capacity", nullable = false)
    @Comment(value = "직무당 모집인원")
    private int capacity;
    
    @Column(name = "position", nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment(value = "직무명")
    private Position position;

    @JoinColumn(name = "recruitment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Recruitment recruitment;

    public void setRecruitment(Recruitment recruitment){
        this.recruitment = recruitment;
    }
}
