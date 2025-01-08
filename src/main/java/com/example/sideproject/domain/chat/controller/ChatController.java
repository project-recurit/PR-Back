package com.example.sideproject.domain.chat.controller;

import com.example.sideproject.domain.chat.dto.*;
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

    @MessageMapping("/room/{roomId}/enter")
    @SendTo("/sub/chat/room/{roomId}")
    public ChatMessageResponse enterRoom(@DestinationVariable Long roomId, @Payload EnterRoomRequest request) {
        log.info("User {} entering room {}", request.getSenderId(), roomId);
        return chatService.enterRoom(roomId, request.getSenderId());
    }

    @MessageMapping("/room/{roomId}/leave")
    @SendTo("/sub/chat/room/{roomId}")
    public ChatMessageResponse leaveRoom(@DestinationVariable Long roomId, @Payload LeaveRoomRequest request) {
        log.info("User {} leaving room {}", request.getSenderId(), roomId);
        return chatService.leaveRoom(roomId, request.getSenderId());
    }

    @PostMapping("/chat/room/{roomId}/read")
    @ResponseBody
    public ResponseEntity<Void> markAsRead(@PathVariable Long roomId, @RequestParam Long userId) {
        chatService.markAsRead(roomId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/chat/room/{roomId}/unread")
    @ResponseBody
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long roomId, @RequestParam Long userId) {
        long count = chatService.getUnreadCount(roomId, userId);
        return ResponseEntity.ok(count);
    }
}
