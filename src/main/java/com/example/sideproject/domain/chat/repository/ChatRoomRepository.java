package com.example.sideproject.domain.chat.repository;

import com.example.sideproject.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT DISTINCT cr FROM ChatRoom cr " +
            "JOIN cr.members u " +
            "WHERE u.member.id = :userId")
    List<ChatRoom> findByUserId(@Param("userId") Long userId);
}