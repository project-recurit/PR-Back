package com.example.sideproject.domain.chat.controller;

import com.example.sideproject.domain.chat.dto.ChatRoomDetailResponse;
import com.example.sideproject.domain.chat.dto.ChatRoomListResponse;
import com.example.sideproject.domain.chat.dto.ChatRoomRequest;
import com.example.sideproject.domain.chat.entity.ChatRoom;
import com.example.sideproject.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    @PostMapping("/create/room")
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
    public ResponseEntity<Page<ChatRoomListResponse>> getRooms(
            @RequestParam Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(chatService.getRooms(memberId, page, size));
    }

    /**
     * 특정 채팅방 조회
     * @param roomId
     * @return
     */
    // TODO 이 컨트롤러를 나중에 채팅방 들어가는걸로 해야하나?
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ChatRoomDetailResponse> getRoom(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        ChatRoomDetailResponse chatRoom = chatService.getChatRoomDetail(roomId, page, size);
        return ResponseEntity.ok(chatRoom);
    }
}
