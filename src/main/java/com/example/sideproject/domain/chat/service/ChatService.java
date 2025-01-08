package com.example.sideproject.domain.chat.service;

import com.example.sideproject.domain.chat.dto.ChatMessageRequest;
import com.example.sideproject.domain.chat.entity.ChatMessage;
import com.example.sideproject.domain.chat.entity.ChatRoom;
import com.example.sideproject.domain.chat.entity.ChatRoomMember;
import com.example.sideproject.domain.chat.repository.ChatMessageRepository;
import com.example.sideproject.domain.chat.repository.ChatRoomRepository;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
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
}