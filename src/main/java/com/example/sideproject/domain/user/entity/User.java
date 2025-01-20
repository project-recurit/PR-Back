package com.example.sideproject.domain.user.entity;

import com.example.sideproject.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.example.sideproject.domain.bookmark.entity.TeamRecruitBookmark;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String socialId;

    private String socialProvider;

    private String email;

    @Column(unique = true)
    private String nickname;

    private String profileUrl;

    @ElementCollection
    @CollectionTable(
            name = "user_tech_stacks",
            joinColumns = @JoinColumn(name = "users_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "tech_stack")
    // 순서 보장이 필요X, 중복 허용X -> Set 사용(List랑 반대)
    private Set<TechStack> techStacks = new HashSet<>();

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

    
    public void addTechStack(Set<TechStack> techStacks) {
        this.techStacks.addAll(techStacks);
    }

    // 북마크 관련 필드 추가
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TeamRecruitBookmark> bookmarks = new HashSet<>();

    public User(String username, String password, String email, String nickname,
                Set<TechStack> techStacks, String socialId, String socialProvider) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.userRole = UserRole.USER;
        this.userStatus = UserStatus.ACTIVE_USER;
        this.lastLoginTime = LocalDateTime.now();
        this.uuid = generateType4UUID();
        if(techStacks != null) {
            this.techStacks = techStacks;
        }
        this.socialId = socialId;
        this.socialProvider = socialProvider;
        this.userRole = UserRole.USER;
        this.userStatus = UserStatus.ACTIVE_USER;
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

    public void updateTechStacks(Set<TechStack> newTechStacks) {
        this.techStacks.clear();
        this.techStacks.addAll(newTechStacks);
    }

    public void updateProfile(String nickname) {
        this.nickname = nickname;
    }


}
