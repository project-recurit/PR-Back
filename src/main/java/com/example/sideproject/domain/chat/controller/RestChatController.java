package com.example.sideproject.domain.chat.controller;

import com.example.sideproject.domain.chat.dto.ChatRoomDetailResponse;
import com.example.sideproject.domain.chat.dto.ChatRoomRequest;
import com.example.sideproject.domain.chat.entity.ChatRoom;
import com.example.sideproject.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class RestChatController {
    private final ChatService chatService;

    /**
     * 채팅방 생성 및 입장
     * @param request
     * @return
     */
    @PostMapping("/creat/room")
    public ResponseEntity<ChatRoom> createRoom(@RequestBody ChatRoomRequest request) {
        ChatRoom chatRoom = chatService.createRoom(request.getSenderId(), request.getReceiverId());
        return ResponseEntity.ok(chatRoom);
    }

    /**
     * 채팅방 조회
     * @param memberId
     * @return
     */
    @GetMapping("/rooms")
    public List<ChatRoom> getRooms(@RequestParam Long memberId) {
        return chatService.getRooms(memberId);
    }

    /**
     * 특정 채팅방 조회
     * @param roomId
     * @return
     */
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ChatRoomDetailResponse> getRoom(@PathVariable Long roomId) {
        ChatRoomDetailResponse chatRoom = chatService.getChatRoomDetail(roomId);
        return ResponseEntity.ok(chatRoom);
    }
}
