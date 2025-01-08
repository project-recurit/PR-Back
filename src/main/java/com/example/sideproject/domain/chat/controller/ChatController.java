package com.example.sideproject.domain.chat.controller;

import com.example.sideproject.domain.chat.dto.ChatMessageRequest;
import com.example.sideproject.domain.chat.dto.ChatMessageResponse;
import com.example.sideproject.domain.chat.dto.ChatRoomRequest;
import com.example.sideproject.domain.chat.entity.ChatMessage;
import com.example.sideproject.domain.chat.entity.ChatRoom;
import com.example.sideproject.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/room/{roomId}/message")
    @SendTo("/sub/chat/room/{roomId}")
    public ChatMessageResponse message(@DestinationVariable Long roomId,
                                                       @Payload ChatMessageRequest message) {
        log.info("Received message for room {}: {}",roomId, message);// 로그 추가
        if (!roomId.equals(message.getRoomId())) {
            throw new IllegalArgumentException("Room id not matched");
        }
        ChatMessage chatMessage = chatService.sendMessage(message);
        return ChatMessageResponse.from(chatMessage);
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
