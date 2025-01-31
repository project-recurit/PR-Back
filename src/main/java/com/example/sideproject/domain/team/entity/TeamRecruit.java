package com.example.sideproject.domain.team.entity;

import com.example.sideproject.domain.user.entity.User;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import com.example.sideproject.domain.user.entity.TechStack;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.util.Set;
import java.util.HashSet;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import com.example.sideproject.global.entity.Timestamped;


@Entity 
@Getter
@NoArgsConstructor
public class TeamRecruit extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String content;

    @ElementCollection
    @CollectionTable(
            name = "team_recruit_tech_stacks",
            joinColumns = @JoinColumn(name = "team_recruit_id")
    )
    private Set<TechStack> techStacks = new HashSet<>();

    private String expectedPeriod;

    private String fileUrl;

    private int viewCount;

    private String contact;

    private int likeCount;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;


    @Builder
    public TeamRecruit(Long id, String title, String content, Set<TechStack> techStacks, String expectedPeriod, String fileUrl, String contact, User user){
        this.id = id;
        this.title = title;
        this.content = content;
        this.techStacks = techStacks;
        this.expectedPeriod = expectedPeriod;
        this.fileUrl = fileUrl;
        this.contact = contact;
        this.user = user;
    }

    public void update(String title, String content, Set<TechStack> techStacks, 
                      String expectedPeriod, String fileUrl, String contact) {
        this.title = title;
        this.content = content;
        this.techStacks = techStacks;
        this.expectedPeriod = expectedPeriod;
        this.fileUrl = fileUrl;
        this.contact = contact;
    }
}
