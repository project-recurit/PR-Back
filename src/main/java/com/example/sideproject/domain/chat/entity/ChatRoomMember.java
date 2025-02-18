package com.example.sideproject.domain.chat.entity;

import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private User member;

    private LocalDateTime lastReadAt;
    private boolean isLeft;
    @Setter
    private LocalDateTime leftAt;

    @Builder
    public ChatRoomMember(User member) {
        this.member = member;
        this.lastReadAt = LocalDateTime.now();
        this.isLeft = false;
    }

    public void setLeft(boolean left) {
        this.isLeft = left;
    }


    public void updateLastRead() {
        this.lastReadAt = LocalDateTime.now();
    }
}
