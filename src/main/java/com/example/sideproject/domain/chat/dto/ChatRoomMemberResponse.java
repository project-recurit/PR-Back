package com.example.sideproject.domain.chat.dto;

import com.example.sideproject.domain.chat.entity.ChatRoomMember;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoomMemberResponse {
    private Long userId;
    private String nickname;
    private LocalDateTime lastReadAt;
    private boolean isLeft;
    private LocalDateTime leftAt;

    public static ChatRoomMemberResponse from(ChatRoomMember member) {
        return ChatRoomMemberResponse.builder()
                .userId(member.getMember().getId())
                .nickname(member.getMember().getNickname())
                .lastReadAt(member.getLastReadAt())
                .isLeft(member.isLeft())
                .leftAt(member.getLeftAt())
                .build();
    }

}
