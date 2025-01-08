package com.example.sideproject.domain.chat.service;

import com.example.sideproject.domain.chat.dto.ChatMessageRequest;
import com.example.sideproject.domain.chat.dto.ChatMessageResponse;
import com.example.sideproject.domain.chat.entity.ChatMessage;
import com.example.sideproject.domain.chat.entity.ChatRoom;
import com.example.sideproject.domain.chat.entity.ChatRoomMember;
import com.example.sideproject.domain.chat.entity.MessageType;
import com.example.sideproject.domain.chat.repository.ChatMessageRepository;
import com.example.sideproject.domain.chat.repository.ChatRoomMemberRepository;
import com.example.sideproject.domain.chat.repository.ChatRoomRepository;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final UserRepository userRepository;

    public ChatRoom createRoom(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.addMember(new ChatRoomMember(sender));
        chatRoom.addMember(new ChatRoomMember(receiver));

        return chatRoomRepository.save(chatRoom);
    }

    public ChatMessage sendMessage(ChatMessageRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));
        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));

        ChatMessage message = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(request.getContent())
                .build();


        return chatMessageRepository.save(message);
    }

    public List<ChatRoom> getRooms(Long userId) {
        return chatRoomRepository.findByUserId(userId);
    }

    // 채팅방 입장
    public ChatMessageResponse enterRoom(Long roomId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

        ChatRoomMember member = findChatRoomMember(roomId, userId);
        markAsRead(roomId, userId);
        member.setLeft(false);

        // 입장 메시지 생성
        ChatMessage enterMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(user)
                .content(user.getNickname() + "님이 입장하셨습니다.")
                .type(MessageType.ENTER)
                .build();

        ChatMessage savedMessage = chatMessageRepository.save(enterMessage);
        return ChatMessageResponse.from(savedMessage);
    }

    // 채팅방 퇴장
    public ChatMessageResponse leaveRoom(Long roomId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

        ChatRoomMember member = findChatRoomMember(roomId, userId);
        member.setLeft(true);
        member.setLeftAt(LocalDateTime.now());

        // 퇴장 메시지 생성
        ChatMessage leaveMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(user)
                .content(user.getNickname() + "님이 퇴장하셨습니다.")
                .type(MessageType.LEAVE)  // 메시지 타입 추가 필요
                .build();

        ChatMessage savedMessage = chatMessageRepository.save(leaveMessage);
        return ChatMessageResponse.from(savedMessage);
    }

    // 메시지 읽음 처리
    public void markAsRead(Long roomId, Long userId) {
        ChatRoomMember member = findChatRoomMember(roomId, userId);
        member.updateLastRead();
    }

    // 읽지 않은 메시지 수 조회
    public long getUnreadCount(Long roomId, Long userId) {
        ChatRoomMember member = findChatRoomMember(roomId, userId);
        return chatMessageRepository.countUnreadMessages(roomId, member.getLastReadAt());
    }

    // 채팅방 멤버 조회 유틸리티 메서드
    private ChatRoomMember findChatRoomMember(Long roomId, Long userId) {
        return chatRoomMemberRepository.findByChatRoomIdAndMemberId(roomId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room member not found"));
    }
}