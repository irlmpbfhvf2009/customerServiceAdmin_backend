package com.lwdevelop.customerServiceAdmin.service;

import java.util.List;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import com.lwdevelop.customerServiceAdmin.dto.ChatMessageDTO;
import com.lwdevelop.customerServiceAdmin.entity.ChatMessage;

public interface ChatMessageService {
    // DB CRUD
    ChatMessage save(ChatMessage chatMessage);

    void saveMessage(ChatMessageDTO chatMessageDTO);

    // custom
    void sendMessage(ChatMessageDTO chatMessageDTO);
    List<ChatMessageDTO> userOnlineStatus(ChatMessageDTO chatMessageDTO, SimpMessageHeaderAccessor headerAccessor);
    
}
