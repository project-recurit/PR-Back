package com.example.sideproject.domain.user.entity;

import com.example.sideproject.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
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

    @ElementCollection
    @CollectionTable(
            name = "user_tech_stacks",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "tech_stack")
    // 순서 보장이 필요X, 중복 허용X -> Set 사용(List랑 반대)
    private Set<TechStack> techStacks = new HashSet<>();

    @ElementCollection
    @CollectionTable(
            name = "user_history",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "user_history")
    private Set<String> userHistory = new HashSet<>();

    /**
     * 추후 연락할 수 있는 서비스에 사용, 카카오톡 링크 or Email에 따라서 다르게 할거임
     */
    private String contact;

    private LocalDateTime lastLoginTime;

    private UserRole userRole;

    private UserStatus userStatus;

    private String refreshToken;

    private String statusUpdate;


    public void addTechStack(Set<TechStack> techStacks) {
        this.techStacks.addAll(techStacks);
    }


    @Builder
    public User(String username ,String password, String email, String nickname, String contact,
                Set<TechStack> techStacks){
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
//        this.profileUrl = profileUrl;
        this.contact = contact;
        this.userRole = UserRole.USER;
        this.userStatus = UserStatus.ACTIVE_USER;
        if(techStacks != null){
            this.techStacks = techStacks;
        }
    }


    public void withDraw() {
        this.userStatus = UserStatus.INACTIVE_USER;
        this.statusUpdate = this.getModifiedAt();
        this.refreshToken = null;
    }

    public boolean logout() {
        refreshToken = null;
        return true;
    }

    public void saveRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


}
