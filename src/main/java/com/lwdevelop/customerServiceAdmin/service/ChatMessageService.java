package com.lwdevelop.customerServiceAdmin.service;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import com.lwdevelop.customerServiceAdmin.dto.ChatMessageDTO;
import com.lwdevelop.customerServiceAdmin.entity.ChatMessage;

public interface ChatMessageService {
    // DB CRUD
    ChatMessage save(ChatMessage chatMessage);

    void saveMessage(ChatMessageDTO chatMessageDTO);

    // custom
    void sendMessageToUser(ChatMessageDTO chatMessageDTO);
    ChatMessageDTO userUpdate(ChatMessageDTO chatMessageDTO, SimpMessageHeaderAccessor headerAccessor);
    
}
