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
@AllArgsConstructor
@Builder
@Entity
@Getter
public class PublicRelation extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @OneToMany(mappedBy = "pr", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<PublicRelationDetail> prDetails;

    @ElementCollection
    @CollectionTable(
            name = "public_relation_tech_stacks",
            joinColumns = @JoinColumn(name = "public_relation_id")
    )
    @BatchSize(size = 20)
    private Set<TechStack> techStacks = new HashSet<>();

    private int viewCount;
    private int likeCount;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    public void addDetails(List<PublicRelationDetail> prDetails) {
        this.prDetails = prDetails;
        addDetails();
    }

    public void addDetails() {
        for (PublicRelationDetail prDetail : prDetails) {
            prDetail.addPr(this);
        }
    }

    public void update(String title, List<PublicRelationDetail> prDetails, Set<TechStack> techStacks) {
        this.title = title;
        this.techStacks = techStacks;
        if (!this.prDetails.isEmpty()) {
            this.prDetails.clear();
            this.prDetails.addAll(prDetails);
            addDetails();
        }
    }

    public void plusViewCount() {
        this.viewCount++;
    }
}

