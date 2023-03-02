package com.lwdevelop.customerServiceAdmin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lwdevelop.customerServiceAdmin.dto.ChatMessageDTO;
import com.lwdevelop.customerServiceAdmin.entity.ChatMessage;
import com.lwdevelop.customerServiceAdmin.repository.ChatMessageRepository;
import com.lwdevelop.customerServiceAdmin.service.ChatMessageService;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {
    
    @Autowired
    private ChatMessageRepository chatMessageRepository;


    @Override
    public List<ChatMessage> findRecentMessages() {
        return chatMessageRepository.findTop10ByOrderByTimestampDesc();
    }

    @Override
    public void saveMessage(ChatMessageDTO chatMessageDTO) {
        throw new UnsupportedOperationException("Unimplemented method 'saveMessage'");
    }

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

}
