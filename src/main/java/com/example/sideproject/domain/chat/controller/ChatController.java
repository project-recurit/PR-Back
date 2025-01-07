package com.example.sideproject.domain.chat.controller;

import com.example.sideproject.domain.chat.dto.ChatMessageRequest;
import com.example.sideproject.domain.chat.dto.ChatRoomRequest;
import com.example.sideproject.domain.chat.entity.ChatMessage;
import com.example.sideproject.domain.chat.entity.ChatRoom;
import com.example.sideproject.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat/message")
    @SendTo("/sub/chat")
    public ChatMessage message(@Payload ChatMessageRequest message,
                               @Header("simpSessionId") String sessionId) {
        return chatService.sendMessage(message);
    }

    /**
     * 채팅방 생성
     * @param request
     * @return
     */
    @PostMapping("/chat/room")
    @ResponseBody
    public ResponseEntity<ChatRoom> createRoom(@RequestBody ChatRoomRequest request) {
        ChatRoom chatRoom = chatService.createRoom(request.getSenderId(), request.getReceiverId());
        return ResponseEntity.ok(chatRoom);
    }

    @GetMapping("/chat/rooms")
    @ResponseBody
    public List<ChatRoom> getRooms(@RequestParam Long memberId) {
        return chatService.getRooms(memberId);
    }
}
