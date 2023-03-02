package com.lwdevelop.customerServiceAdmin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lwdevelop.customerServiceAdmin.entity.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    List<ChatMessage> findAllByOrderByTimestampAsc();

    List<ChatMessage> findTop10ByOrderByTimestampDesc();
    
}
