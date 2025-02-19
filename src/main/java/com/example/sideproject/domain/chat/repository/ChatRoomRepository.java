package com.example.sideproject.domain.chat.repository;

import com.example.sideproject.domain.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT cr FROM ChatRoom cr " +
            "JOIN cr.members m " +
            "WHERE m.member.id = :userId " +
            "ORDER BY cr.lastMessage.sentAt DESC NULLS LAST")
    Page<ChatRoom> findByUserId(@Param("userId") Long userId, Pageable pageable);
}