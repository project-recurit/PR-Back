package com.example.sideproject.domain.chat.controller;

import com.example.sideproject.domain.chat.dto.ChatRoomRequest;
import com.example.sideproject.domain.chat.entity.ChatRoom;
import com.example.sideproject.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestChatController {
    private final ChatService chatService;

    /**
     * 채팅방 생성 및 입장
     * @param request
     * @return
     */
    @PostMapping("/chat/room")
    public ResponseEntity<ChatRoom> createRoom(@RequestBody ChatRoomRequest request) {
        ChatRoom chatRoom = chatService.createRoom(request.getSenderId(), request.getReceiverId());
        return ResponseEntity.ok(chatRoom);
    }
}
