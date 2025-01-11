package com.example.sideproject.domain.pr.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



import com.example.sideproject.domain.user.entity.TechStack;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.entity.Timestamped;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class PublicRelation extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @OneToMany(mappedBy = "pr", orphanRemoval = true, cascade = CascadeType.ALL)
    @BatchSize(size = 20)
    private List<PublicRelationDetail> prDetails;

    @OneToMany(mappedBy = "publicRelation", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    private List<PublicRelationTechStacks> techStacks;

    private int viewCount;
    private int likeCount;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    @Builder
    public PublicRelation(long id, String title, List<PublicRelationDetail> prDetails, Set<TechStack> techStacks, int viewCount, int likeCount, User user) {
        this.id = id;
        this.title = title;
        this.prDetails = prDetails;
        this.techStacks = toTechStacks(techStacks);
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.user = user;
    }

    public void addDetails(List<PublicRelationDetail> prDetails) {
        this.prDetails = prDetails;
        addDetails();
    }

    public void addDetails() {
        for (PublicRelationDetail prDetail : prDetails) {
            prDetail.addPr(this);
        }
    }

    private List<PublicRelationTechStacks> toTechStacks(Set<TechStack> techStacks) {
        List<PublicRelationTechStacks> result = new ArrayList<>();
        for (TechStack techStack : techStacks) {
            result.add(
                    PublicRelationTechStacks.builder()
                            .publicRelation(this)
                            .techStack(techStack)
                            .build()
            );
        }
        return result;
    }

    public void update(String title, List<PublicRelationDetail> prDetails, Set<TechStack> techStacks) {
        this.title = title;

        this.techStacks.clear();
        this.techStacks.addAll(toTechStacks(techStacks));

        this.prDetails.clear();
        this.prDetails.addAll(prDetails);
        addDetails();
    }

    public void plusViewCount() {
        this.viewCount++;
    }

    public List<TechStack> getTechStackList() {
        return techStacks.stream().map(PublicRelationTechStacks::getTechStack).toList();
    }
}

