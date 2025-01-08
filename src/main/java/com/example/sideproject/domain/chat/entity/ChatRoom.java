package com.example.sideproject.domain.chat.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatRoomMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> messages = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "last_message_id")
    private ChatMessage lastMessage;

    private LocalDateTime createdAt;

    public void addMember(ChatRoomMember member) {
        this.members.add(member);
        member.setChatRoom(this);  // 양방향 관계 설정
    }

    public void updateLastMessage(ChatMessage message) {
        this.lastMessage = message;
    }

    @Builder
    public ChatRoom() {

        this.createdAt = LocalDateTime.now();
    }
}