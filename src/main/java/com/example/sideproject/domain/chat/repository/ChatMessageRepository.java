package com.example.sideproject.domain.chat.repository;


import com.example.sideproject.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT COUNT(m) FROM ChatMessage m " +
            "WHERE m.chatRoom.id = :roomId " +
            "AND m.sentAt > :lastReadAt " +
            "AND m.read = false")
    long countUnreadMessages(@Param("roomId") Long roomId, @Param("lastReadAt") LocalDateTime lastReadAt);

    @Query("SELECT m FROM ChatMessage m " +
            "WHERE m.chatRoom.id = :roomId " +
            "AND m.sentAt > :lastReadAt " +
            "AND m.read = false " +
            "ORDER BY m.sentAt ASC")
    List<ChatMessage> findUnreadMessages(
            @Param("roomId") Long roomId,
            @Param("lastReadAt") LocalDateTime lastReadAt
    );

    @Modifying  // 벌크 업데이트를 위한 어노테이션 추가
    @Query("UPDATE ChatMessage m SET m.read = true " +
            "WHERE m.chatRoom.id = :roomId " +
            "AND m.sentAt > :lastReadAt " +
            "AND m.read = false")
    void markMessagesAsRead(
            @Param("roomId") Long roomId,
            @Param("lastReadAt") LocalDateTime lastReadAt
    );

    @Query("SELECT m FROM ChatMessage m " +
            "WHERE m.chatRoom.id = :roomId " +
            "ORDER BY m.sentAt ASC")
    List<ChatMessage> findAllByChatRoomIdOrderBySentAtAsc(@Param("roomId") Long roomId);



}
