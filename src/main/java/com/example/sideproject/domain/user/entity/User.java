package com.example.sideproject.domain.user.entity;

import com.example.sideproject.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long id;

    private String username;

    private String password;

    private String email;

    private String nickname;

    private String profileUrl;

    private String name;

    @ElementCollection
    @CollectionTable(
            name = "user_tech_stacks",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "tech_stack")
    // 순서 보장이 필요X, 중복 허용X -> Set 사용(List랑 반대)
    private Set<TechStack> techStacks = new HashSet<>();

    private String headline;

    private String phoneNumber;

    private LocalDateTime lastLoginTime;

    private UserRole userRole;


    public User(String username, String password, String email, String nickname,
               String name, String headline, String phoneNumber){
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
//        this.profileUrl = profileUrl;
        this.name = name;
//        this.userTechStack = userTechStack;
        this.headline = headline;
        this.phoneNumber = phoneNumber;
        this.userRole = UserRole.USER;
    }


}
