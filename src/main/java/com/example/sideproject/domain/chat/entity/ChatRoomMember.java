package com.example.sideproject.domain.chat.entity;

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

    //Todo 필드로 추가 해야함
    private Long unReadCount;

    private LocalDateTime lastReadAt;
    private LocalDateTime leftAt;
    private boolean isLeft;


    @Builder
    public ChatRoomMember(User member) {
        this.member = member;
        this.lastReadAt = LocalDateTime.now();
        this.isLeft = false;
    }

    public void setLeft(boolean left) {
        this.isLeft = left;
    }

    public void setLeftAt(LocalDateTime leftAt) {
        this.leftAt = leftAt;
    }


    public void updateLastRead() {
        this.lastReadAt = LocalDateTime.now();
    }
}
