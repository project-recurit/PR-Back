package com.example.sideproject.domain.chat.service;

import com.example.sideproject.domain.chat.dto.*;
import com.example.sideproject.domain.chat.entity.*;
import com.example.sideproject.domain.chat.repository.ChatMessageRepository;
import com.example.sideproject.domain.chat.repository.ChatRoomMemberRepository;
import com.example.sideproject.domain.chat.repository.ChatRoomRepository;
import com.example.sideproject.domain.notification.service.ChatNotificationService;
import com.example.sideproject.domain.pr.repository.PublicResumesRepository;
import com.example.sideproject.domain.project.entity.Project;
import com.example.sideproject.domain.project.repository.ProjectRepository;
import com.example.sideproject.domain.user.entity.User;
import com.example.sideproject.domain.user.repository.UserRepository;
import com.example.sideproject.global.config.WebSocketEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final UserRepository userRepository;
    private final WebSocketEventHandler webSocketEventHandler;
    private final ProjectRepository projectRepository;
    private final PublicResumesRepository publicResumesRepository;
    private final ChatNotificationService chatNotificationService;

    /**
     * 채팅방 생성
     * @param senderId
     * @param receiverId
     * @return
     */
    @Transactional()
    public ChatRoom createRoom(Long senderId, Long receiverId, ChatRoomType chatRoomType,Long referenceId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        if (chatRoomType == ChatRoomType.PROJECT) {
            projectRepository.findById(referenceId)
                    .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        } else if (chatRoomType == ChatRoomType.PR) {
            publicResumesRepository.findById(referenceId)
                    .orElseThrow(() -> new IllegalArgumentException("PublicResume not found"));
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .type(chatRoomType)
                .referenceId(referenceId)
                .build();
        chatRoom.addMember(new ChatRoomMember(sender));
        chatRoom.addMember(new ChatRoomMember(receiver));

        chatNotificationService.createRoom(sender.getNickname(), referenceId, receiverId);

        return chatRoomRepository.save(chatRoom);
    }

    /**
     * 메시지 보내기
     * @param request
     * @return
     */
    @Transactional
    public ChatMessageResponse sendMessage(ChatMessageRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));
        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));

        ChatMessage message = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(request.getContent())
                .type(MessageType.CHAT)
                .build();

        ChatMessage savedMessage = chatMessageRepository.save(message);
        chatRoom.updateLastMessage(savedMessage);
        chatRoomRepository.save(chatRoom);

        autoMarkAsReadForActiveUsers(chatRoom);

        Map<Long, Long> unreadCounts = getUnreadCountsForMembers(chatRoom);
        log.info("{} unread messages have been sent", unreadCounts.size());
        return ChatMessageResponse.from(savedMessage);
    }

    /**
     * 채팅방 세션 종료(뒤로가기)
     * @param roomId
     * @param userId
     */
    //TODO : 웹소켓 끊어질 때 가장 마지막 상대방의 메시지를 보낸 시간으로
    @Transactional
    public void disconnectFromRoom(Long roomId, Long userId) {
        ChatRoomMember member = findChatRoomMember(roomId, userId);
        member.updateLastRead(); // 마지막 읽은 시간 업데이트
    }
    /**
     * 내가 있는 채팅방 불러오기
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public Page<ChatRoomListResponse> getRooms(Long userId, int page, int size) {
        Page<ChatRoom> chatRooms = chatRoomRepository.findByUserId(
                userId,
                PageRequest.of(page, size, Sort.by("lastMessage.sentAt").descending())
        );

        return chatRooms.map(chatRoom ->
                ChatRoomListResponse.from(
                        chatRoom,
                        getUnreadCount(chatRoom.getId(), userId)
                )
        );
    }

    /**
     * 채팅방 입장
     * @param roomId
     * @param userId
     * @return
     */
    @Transactional
    public ChatMessageResponse enterRoom(Long roomId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

        ChatRoomMember member = findChatRoomMember(roomId, userId);
        //Todo 읽음처리 안되는거 수정 필요
        markAllMessagesAsRead(roomId, member);
        member.setLeft(false);

        // 입장 메시지 생성
        ChatMessage enterMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(user)
                .content(user.getNickname())
                .type(MessageType.ENTER)
                .build();

        ChatMessage savedMessage = chatMessageRepository.save(enterMessage);
        return ChatMessageResponse.from(savedMessage);
    }

    /**
     * 채팅방 퇴장
     * @param roomId
     * @param userId
     * @return
     */
    // TODO 채팅방 나가기 했을 때 채팅 보내지 못하게
    @Transactional
    public ChatMessageResponse leaveRoom(Long roomId, Long userId, String sessionId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

        ChatRoomMember member = findChatRoomMember(roomId, userId);
        member.setLeft(true);
        member.setLeftAt(LocalDateTime.now());

        ChatMessage leaveMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(user)
                .content(user.getNickname() + "님이 퇴장하셨습니다.")
                .type(MessageType.LEAVE)
                .build();

        ChatMessage savedMessage = chatMessageRepository.save(leaveMessage);
        webSocketEventHandler.removeUserChatSession(sessionId);
        return ChatMessageResponse.from(savedMessage);
    }

    /**
     * 특정 채팅방 조회
     * @param roomId
     * @return
     */
    @Transactional(readOnly = true)
    public ChatRoomDetailResponse getChatRoomDetail(Long roomId, int page, int size) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("sentAt").ascending());
        Page<ChatMessage> messages = chatMessageRepository
                .findAllByChatRoomIdOrderBySentAtAsc(roomId, pageable);

        List<ChatMessageResponse> messageResponses = messages.getContent().stream()
                .map(ChatMessageResponse::from)
                .collect(Collectors.toList());

        ContentSummaryResponse referenceInfo = getReferenceSummary(chatRoom.getType(), chatRoom.getReferenceId());

        return ChatRoomDetailResponse.of(
                chatRoom,
                messageResponses,
                messages,
                referenceInfo
        );
    }

    /**
     * 메시지 읽음처리
     * @param roomId
     * @param userId
     */
    public void markAsRead(Long roomId, Long userId) {
        ChatRoomMember member = findChatRoomMember(roomId, userId);
        member.updateLastRead();
    }

    /**
     * 읽지 않은 메시지 카운트
     * @param roomId
     * @param userId
     * @return
     */
    public Long getUnreadCount(Long roomId, Long userId) {
        ChatRoomMember member = findChatRoomMember(roomId, userId);
        return chatMessageRepository.countUnreadMessages(roomId, member.getLastReadAt());
    }

    // 채팅방 멤버 조회 유틸리티 메서드
    private ChatRoomMember findChatRoomMember(Long roomId, Long userId) {
        return chatRoomMemberRepository.findByChatRoomIdAndMemberId(roomId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room member not found"));
    }

    /**
     * 모든 메시지 읽음 처리
     * @param roomId
     * @param member
     */
    private void markAllMessagesAsRead(Long roomId, ChatRoomMember member) {
        chatMessageRepository.markMessagesAsRead(roomId, member.getLastReadAt());
        member.updateLastRead();
    }

    /**
     * 멤버별 읽지 않은 메시지 카운트
     * @param chatRoom
     * @return
     */
    //필드로 빼자
    private Map<Long, Long> getUnreadCountsForMembers(ChatRoom chatRoom) {
        return chatRoom.getMembers().stream()
                .collect(Collectors.toMap(
                        member -> member.getMember().getId(),
                        member -> chatMessageRepository.countUnreadMessages(
                                chatRoom.getId(),
                                member.getLastReadAt()
                        )
                ));
    }

    /**
     * 채팅방에 현재 connect되어있는 멤버 찾아서 읽음처리 진행
     * @param chatRoom
     */
    private void autoMarkAsReadForActiveUsers(ChatRoom chatRoom) {
        chatRoom.getMembers().stream()
                .map(member -> member.getMember().getId())
                .filter(userId -> WebSocketEventHandler.isUserInRoom(userId, chatRoom.getId()))
                .forEach(userId -> markAsRead(chatRoom.getId(), userId));
    }

    /**
     * 채팅방 타입과 참조 ID에 따라 참조 객체의 요약 정보를 가져옴
     * @param type 채팅방 타입 (PROJECT 또는 PR)
     * @param referenceId 참조 ID (프로젝트 ID 또는 이력서 ID)
     * @return 요약 정보 객체 (ContentSummaryResponse)
     */
    public ContentSummaryResponse getReferenceSummary(ChatRoomType type, Long referenceId) {
        if (type == ChatRoomType.PROJECT) {
            return projectRepository.findById(referenceId)
                    .map(ContentSummaryResponse::from)
                    .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + referenceId));
        } else if (type == ChatRoomType.PR) {
            return publicResumesRepository.findById(referenceId)
                    .map(ContentSummaryResponse::from)
                    .orElseThrow(() -> new IllegalArgumentException("PublicResume not found with id: " + referenceId));
        }
        throw new IllegalArgumentException("Unsupported chat room type: " + type);
    }

    /**
     * 채팅방의 참조 객체 요약 정보를 가져옴
     * @param chatRoom 채팅방
     * @return 요약 정보 객체 (ContentSummaryResponse)
     */
    public ContentSummaryResponse getReferenceSummary(ChatRoom chatRoom) {
        return getReferenceSummary(chatRoom.getType(), chatRoom.getReferenceId());
    }

//    /**
//     * 프로젝트와 관련된 채팅방 개수 조회
//     * @param projectId 프로젝트 ID
//     * @return 채팅방 개수
//     */
//    @Transactional(readOnly = true)
//    public long countChatRoomsByProjectId(Long projectId) {
//        return chatRoomRepository.countByProjectId(projectId);
//    }
//
//    /**
//     * 공개 이력서와 관련된 채팅방 개수 조회
//     * @param prId 공개 이력서 ID
//     * @return 채팅방 개수
//     */
//    @Transactional(readOnly = true)
//    public long countChatRoomsByPublicResumeId(Long prId) {
//        return chatRoomRepository.countByPublicResumeId(prId);
//    }
//
//    /**
//     * 특정 사용자의 프로젝트와 관련된 채팅방 개수 조회
//     * @param projectId 프로젝트 ID
//     * @param userId 사용자 ID
//     * @return 채팅방 개수
//     */
//    @Transactional(readOnly = true)
//    public long countChatRoomsByProjectIdAndUserId(Long projectId, Long userId) {
//        return chatRoomRepository.countByProjectIdAndUserId(projectId, userId);
//    }
//
//    /**
//     * 특정 사용자의 공개 이력서와 관련된 채팅방 개수 조회
//     * @param prId 공개 이력서 ID
//     * @param userId 사용자 ID
//     * @return 채팅방 개수
//     */
//    @Transactional(readOnly = true)
//    public long countChatRoomsByPublicResumeIdAndUserId(Long prId, Long userId) {
//        return chatRoomRepository.countByPublicResumeIdAndUserId(prId, userId);
//    }
}