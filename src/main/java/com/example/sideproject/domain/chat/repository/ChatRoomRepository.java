package com.example.sideproject.domain.chat.repository;

import com.example.sideproject.domain.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT cr FROM ChatRoom cr " +
            "JOIN cr.members m " +
            "WHERE m.member.id = :userId " +
            "ORDER BY cr.lastMessage.sentAt DESC NULLS LAST")
    Page<ChatRoom> findByUserId(@Param("userId") Long userId, Pageable pageable);

    // Project와 관련된 채팅방 개수 조회
//    @Query("SELECT COUNT(cr) FROM ChatRoom cr WHERE cr.type = com.example.sideproject.domain.pr.entity.ChatRoomType.PROJECT AND cr.referenceId = :projectId")
//    long countByProjectId(@Param("projectId") Long projectId);
//
//    // PublicResumes와 관련된 채팅방 개수 조회
//    @Query("SELECT COUNT(cr) FROM ChatRoom cr WHERE cr.type = com.example.sideproject.domain.pr.entity.ChatRoomType.PR AND cr.referenceId = :prId")
//    long countByPublicResumeId(@Param("prId") Long prId);
//
//    // 특정 사용자의 Project와 관련된 채팅방 개수 조회
//    @Query("SELECT COUNT(cr) FROM ChatRoom cr JOIN cr.members m WHERE cr.type = com.example.sideproject.domain.pr.entity.ChatRoomType.PROJECT AND cr.referenceId = :projectId AND m.member.id = :userId")
//    long countByProjectIdAndUserId(@Param("projectId") Long projectId, @Param("userId") Long userId);
//
//    // 특정 사용자의 PublicResumes와 관련된 채팅방 개수 조회
//    @Query("SELECT COUNT(cr) FROM ChatRoom cr JOIN cr.members m WHERE cr.type = com.example.sideproject.domain.pr.entity.ChatRoomType.PR AND cr.referenceId = :prId AND m.member.id = :userId")
//    long countByPublicResumeIdAndUserId(@Param("prId") Long prId, @Param("userId") Long userId);
}
