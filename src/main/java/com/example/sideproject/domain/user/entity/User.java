package com.example.sideproject.domain.user.entity;

import com.example.sideproject.domain.techstack.entity.TechStack;
import com.example.sideproject.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

import com.example.sideproject.domain.bookmark.entity.TeamRecruitBookmark;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long id;

//    @Column(unique = true)
    private String username;

    private String password;

    private String socialId;

    private String socialProvider;

    private String email;

//    @Column(unique = true)
    private String nickname;

    private String profileUrl;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTechStack> userTechStacks = new ArrayList<>();

    // 경력
    @ElementCollection
    @CollectionTable(
            name = "users_history",
            joinColumns = @JoinColumn(name = "users_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "users_history")
    private Set<String> usersHistory = new HashSet<>();

    private LocalDateTime lastLoginTime;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    private String refreshToken;

    private String statusUpdate;

    @Column(unique = true, nullable = false)
    private UUID uuid;

    private String position;


    public void addTechStack(List<UserTechStack> userTechStacks) {
        this.userTechStacks.addAll(userTechStacks);
    }

    // 북마크 관련 필드 추가
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TeamRecruitBookmark> bookmarks = new HashSet<>();

    @Builder
    public User(String username, String password, String email, String nickname,
                List<UserTechStack> userTechStacks, String socialId, String socialProvider, String position) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.userRole = UserRole.USER;
        this.userStatus = UserStatus.ACTIVE_USER;
        this.lastLoginTime = LocalDateTime.now();
        this.uuid = generateType4UUID();
        if(userTechStacks != null) {
            this.userTechStacks = userTechStacks;
        }
        this.socialId = socialId;
        this.socialProvider = socialProvider;
        this.position = position;
    }

    public User(Long id) {
        this.id = id;
    }


    public void withDraw() {
        this.userStatus = UserStatus.WITHDRAW_USER;
        this.statusUpdate = this.getModifiedAt();
        this.refreshToken = null;
    }

    // 로그인 정보 업데이트
    public void updateLoginInfo(String refreshToken, LocalDateTime loginTime) {
        this.refreshToken = refreshToken;
        this.lastLoginTime = loginTime;
    }

    // 로그아웃 시 리프레시 토큰 제거
    public void logout() {
        this.refreshToken = null;
    }

    // 리프레시 토큰 업데이트
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void saveRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    private UUID generateType4UUID(){
        UUID userUuid = UUID.randomUUID();
        return userUuid;
    }

    public void setLogin(){
        this.lastLoginTime = LocalDateTime.now();
    }

    public boolean isActive() {
        return this.userStatus == UserStatus.ACTIVE_USER;
    }

    public void updateTechStacks(List<UserTechStack> newUserTechStacks) {
        this.userTechStacks.clear();
        this.userTechStacks.addAll(newUserTechStacks);
        // UserTechStack의 user 필드도 업데이트
        newUserTechStacks.forEach(techStack -> techStack.setUser(this));
    }

    public void updateProfile(String nickname) {
        this.nickname = nickname;
    }


}
