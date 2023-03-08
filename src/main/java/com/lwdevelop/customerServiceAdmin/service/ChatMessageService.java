package com.lwdevelop.customerServiceAdmin.service;

import java.util.List;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import com.lwdevelop.customerServiceAdmin.dto.ChatMessageDTO;
import com.lwdevelop.customerServiceAdmin.entity.ChatMessage;

public interface ChatMessageService {
    // DB CRUD
    ChatMessage save(ChatMessage chatMessage);

    void saveMessage(ChatMessageDTO chatMessageDTO);

    // custom
    void sendMessageToUser(ChatMessageDTO chatMessageDTO);
    ChatMessageDTO handleMessage(ChatMessageDTO chatMessageDTO, SimpMessageHeaderAccessor headerAccessor);
    ChatMessageDTO userUpdate(ChatMessageDTO chatMessageDTO, SimpMessageHeaderAccessor headerAccessor);
    ChatMessageDTO assignUser(ChatMessageDTO chatMessageDTO);
    
}
