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

    /**
     * 메시지 보내기
     * @param roomId
     * @param message
     * @return
     */
    @MessageMapping("/room/{roomId}/message")
    @SendTo("/sub/chat/room/{roomId}")
    public ChatMessageResponse message(@DestinationVariable Long roomId,
                                                       @Payload ChatMessageRequest message) {
        log.info("Received message for room {}: {}",roomId, message);// 로그 추가
        if (!roomId.equals(message.getRoomId())) {
            throw new IllegalArgumentException("Room id not matched");
        }
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

    /**
     * 채팅방 조회
     * @param memberId
     * @return
     */
    @GetMapping("/chat/rooms")
    @ResponseBody
    public List<ChatRoom> getRooms(@RequestParam Long memberId) {
        return chatService.getRooms(memberId);
    }

    /**
     * 채팅방 입장
     * @param roomId
     * @param request
     * @return
     */
    @MessageMapping("/room/{roomId}/enter")
    @SendTo("/sub/chat/room/{roomId}")
    public ChatMessageResponse enterRoom(@DestinationVariable Long roomId, @Payload EnterRoomRequest request) {
        log.info("User {} entering room {}", request.getSenderId(), roomId);
        return chatService.enterRoom(roomId, request.getSenderId());
    }

    /**
     * 채팅방 퇴장
     * @param roomId
     * @param request
     * @return
     */
    @MessageMapping("/room/{roomId}/leave")
    @SendTo("/sub/chat/room/{roomId}")
    public ChatMessageResponse leaveRoom(@DestinationVariable Long roomId, @Payload LeaveRoomRequest request) {
        log.info("User {} leaving room {}", request.getSenderId(), roomId);
        return chatService.leaveRoom(roomId, request.getSenderId());
    }

    /**
     * 읽지 않은 채팅 읽음처리
     * @param roomId
     * @param userId
     * @return
     */
    @PostMapping("/chat/room/{roomId}/read")
    @ResponseBody
    public ResponseEntity<Void> markAsRead(@PathVariable Long roomId, @RequestParam Long userId) {
        chatService.markAsRead(roomId, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * 읽지 않은 채팅 카운팅
     * @param roomId
     * @param userId
     * @return
     */
    @GetMapping("/chat/room/{roomId}/unread")
    @ResponseBody
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long roomId, @RequestParam Long userId) {
        long count = chatService.getUnreadCount(roomId, userId);
        return ResponseEntity.ok(count);
    }
}
