package com.example.sideproject.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class User extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long id;

    private String username;

    private String password;

    private String email;

    private String nickname;

    private String profileUrl;

    private String name;

    @Enumerated(EnumType.STRING)
    private UserTechStack  userTechStack;

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
