package com.example.sideproject.domain.personal.entity;

import java.util.HashSet;
import java.util.Set;



import com.example.sideproject.domain.user.entity.TechStack;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.global.entity.Timestamped;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class ProjectRecruit extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String content;

    @ElementCollection
    @CollectionTable(
            name = "project_recruit_tech_stacks",
            joinColumns = @JoinColumn(name = "project_recruit_id")
    )
    private Set<TechStack> techStacks = new HashSet<>();

    private String expectedPeriod;
    private String fileUrl;
    private int viewCount;
    private String contact;
    private int likeCount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ProjectRecruit(String title, String content, Set<TechStack> techStacks, 
                         String expectedPeriod, String fileUrl, String contact, User user) {
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