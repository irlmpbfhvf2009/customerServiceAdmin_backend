package com.lwdevelop.customerServiceAdmin.service;

import java.util.List;

import com.lwdevelop.customerServiceAdmin.dto.ChatMessageDTO;
import com.lwdevelop.customerServiceAdmin.entity.ChatMessage;

public interface ChatMessageService {
    ChatMessage save(ChatMessage chatMessage);
    List<ChatMessage> findRecentMessages();
    void saveMessage(ChatMessageDTO chatMessageDTO);
}
