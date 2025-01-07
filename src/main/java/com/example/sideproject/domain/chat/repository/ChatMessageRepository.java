package com.example.sideproject.domain.chat.repository;


import com.example.sideproject.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

}
