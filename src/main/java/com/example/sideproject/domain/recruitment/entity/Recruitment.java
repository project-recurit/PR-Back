package com.example.sideproject.domain.recruitment.entity;

import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.entity.Timestamped;
import com.example.sideproject.global.enums.WorkType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Getter
@NoArgsConstructor
public class Recruitment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Comment(value = "제목")
    private String title;

    @Column(nullable = false)
    @Comment(value = "내용")
    private String content;

    @Column(nullable = true, name = "dead_line") // 모집 기간
    @Comment(value = "모집 기간")
    private String deadLine;

    @OneToMany(mappedBy = "recruitment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecruitmentImage> fileImages = new ArrayList<>();

    @OneToMany(mappedBy = "recruitment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecruitmentTechStack> recruitmentTechStacks = new ArrayList<>();

    @Column(nullable = false, name = "view_count")
    @Comment(value = "조회수")
    private int viewCount;

    @Column(nullable = false, name = "comment_count")
    @Comment(value = "댓글 갯수")
    private int commentCount;

    @Column(nullable = false, name = "favorite_count")
    @Comment(value = "좋아요 갯수")
    private int favoriteCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private User user;

    @Column(nullable = false, name = "estimated_duration") // 예상 기간
    @Enumerated(EnumType.STRING)
    @Comment(value = "예상 기간")
    private EstimatedDuration estimatedDuration;

    @Column(nullable = false, name = "is_recruiting") // 진행 여부
    @Comment(value = "진행 여부")
    private boolean isRecruiting;

    @Column(nullable = false, name = "work_type") // 진행 방식
    @Enumerated(EnumType.STRING)
    @Comment(value = "진행 방식")
    private WorkType workType;

    @Column(nullable = false, name = "is_commercial")
    @Comment(value = "상업 목적")
    private boolean isCommercial;

    @Column(nullable = false, name = "recruitment_category")
    @Enumerated(EnumType.STRING)
    @Comment(value = "카테고리")
    private RecruitmentCategory recruitmentCategory;

    @OneToMany(mappedBy = "recruitment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Comment(value = "직무")
    private List<RecruitmentPosition> positions = new ArrayList<>();

    public void addPosition(RecruitmentPosition position) {
        this.positions.add(position);
        position.setRecruitment(this);
    }

    public void addRecruitmentTechStacks(RecruitmentTechStack recruitmentTechStack) {
        this.recruitmentTechStacks.add(recruitmentTechStack);
        recruitmentTechStack.setRecruitment(this);
    }


    @Builder
    public Recruitment(String title, String content, String deadLine,
                       EstimatedDuration estimatedDuration, boolean isRecruiting,
                       WorkType workType, User user, List<RecruitmentTechStack> recruitmentTechStacks,
                       List<RecruitmentImage> recruitmentImages, int viewCount, int commentCount, int favoriteCount, Long id,
                       boolean isCommercial, RecruitmentCategory recruitmentCategory) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.deadLine = deadLine;
        this.estimatedDuration = estimatedDuration;
        this.isRecruiting = isRecruiting;
        this.workType = workType;
        this.user = user;
        this.recruitmentTechStacks = recruitmentTechStacks != null ? recruitmentTechStacks : new ArrayList<>();
        this.fileImages = recruitmentImages != null ? recruitmentImages : new ArrayList<>();
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.favoriteCount = favoriteCount;
        this.isCommercial = isCommercial;
        this.recruitmentCategory = recruitmentCategory;
    }

    public void addCommentCount() {
        this.commentCount = this.commentCount + 1;
    }
    public void downCommentCount() {
        this.commentCount = this.commentCount - 1;
    }
    public boolean isRecruitmentLeader(Long leaderId) {
        return Objects.equals(user.getId(), leaderId);
    }
}
