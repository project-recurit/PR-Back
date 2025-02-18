package com.example.sideproject.domain.chat.entity;

import com.example.sideproject.domain.project.entity.Project;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private LocalDateTime createdAt;

    public void addMember(ChatRoomMember member) {
        this.members.add(member);
        member.setChatRoom(this);  // 양방향 관계 설정
    }

    public void updateLastMessage(ChatMessage message) {
        this.lastMessage = message;
    }

    @Builder
    public ChatRoom(Project project) {
        this.project = project;
        this.createdAt = LocalDateTime.now();
    }
}