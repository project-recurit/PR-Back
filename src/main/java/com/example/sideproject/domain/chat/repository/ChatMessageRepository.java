package com.example.sideproject.domain.chat.repository;


import com.example.sideproject.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT COUNT(m) FROM ChatMessage m " +
            "WHERE m.chatRoom.id = :roomId " +
            "AND m.sentAt > :lastReadAt")
    long countUnreadMessages(@Param("roomId") Long roomId, @Param("lastReadAt") LocalDateTime lastReadAt);

    @Query("SELECT m FROM ChatMessage m " +
            "WHERE m.chatRoom.id = :roomId " +
            "AND m.sentAt > :lastReadAt " +
            "AND m.read = false")
    List<ChatMessage> findUnreadMessages(
            @Param("roomId") Long roomId,
            @Param("lastReadAt") LocalDateTime lastReadAt
    );
}
