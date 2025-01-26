package com.example.sideproject.global.config;

import com.example.sideproject.domain.chat.dto.UserChatSession;
import com.example.sideproject.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventHandler {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private static final Map<String, UserChatSession> chatSessions = new ConcurrentHashMap<>();

    /**
     * 웹소켓 연결 핸들러
     * @param event
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        log.info("New web socket session : {}", sessionId);
    }

    /**
     * 웹소켓 끊어짐 핸들러
     * @param event
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        UserChatSession userChatSession = chatSessions.remove(sessionId);
        if (userChatSession != null) {
            log.info("User disconnected : {} from room : {}", userChatSession.getUserId(), userChatSession.getRoomId());
        }
    }

    /**
     * 채팅방 세션 종료(뒤로가기)
     * @param sessionId
     */
    public static void removeUserChatSession(String sessionId) {
        chatSessions.remove(sessionId);
    }

    /**
     * 세션ID 있는 사용자 Map에 저장
     * @param sessionId
     * @param userId
     * @param roomId
     */
    public static void addUserChatSession(String sessionId, Long userId, Long roomId) {
        chatSessions.put(sessionId, new UserChatSession(userId, roomId));
    }

    /**
     * 채팅방에 유저 있는지 판별
     * @param userId
     * @param roomId
     * @return boolean
     */
    public static boolean isUserInRoom(Long userId, Long roomId) {
        return chatSessions.values().stream()
                .anyMatch(session -> session.getUserId().equals(userId)
                        && session.getRoomId().equals(roomId));
    }
}
