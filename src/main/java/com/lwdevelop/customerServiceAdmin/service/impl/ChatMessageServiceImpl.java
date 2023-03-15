package com.lwdevelop.customerServiceAdmin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.lwdevelop.customerServiceAdmin.dto.ChatMessageDTO;
import com.lwdevelop.customerServiceAdmin.entity.ChatMessage;
import com.lwdevelop.customerServiceAdmin.repository.ChatMessageRepository;
import com.lwdevelop.customerServiceAdmin.service.ChatMessageService;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Override
    public void saveMessage(ChatMessageDTO chatMessageDTO) {
        throw new UnsupportedOperationException("Unimplemented method 'saveMessage'");
    }

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

    // 線上用戶列表
    private static List<ChatMessageDTO> onlineUsers = new ArrayList<>();

    @Override
    public void sendMessage(ChatMessageDTO chatMessageDTO) {
        System.out.println(chatMessageDTO);
        messagingTemplate.convertAndSendToUser(chatMessageDTO.getReceiver(), "/queue/messages", chatMessageDTO);
    }

    @Override
    public List<ChatMessageDTO> userOnlineStatus(ChatMessageDTO chatMessageDTO,
            SimpMessageHeaderAccessor headerAccessor) {

        String ip = (String) headerAccessor.getSessionAttributes().get("ip");
        String sender = chatMessageDTO.getSender();
        chatMessageDTO.setIp(ip);

        if (chatMessageDTO.getIsOnline()) {
            onlineUsers.add(chatMessageDTO);
        } else {
            onlineUsers.removeIf(user -> user.getSender().equals(sender));
        }
        return onlineUsers;
    }

}
