package com.lwdevelop.customerServiceAdmin.service.impl;

import java.util.List;

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

    @Override
    public void sendMessageToUser(ChatMessageDTO chatMessageDTO) {
        messagingTemplate.convertAndSendToUser(chatMessageDTO.getReceiver(), "/queue/messages",
        chatMessageDTO);
    }


    @Override
    public ChatMessageDTO handleMessage(ChatMessageDTO chatMessageDTO, SimpMessageHeaderAccessor headerAccessor) {
        String ip = (String) headerAccessor.getSessionAttributes().get("ip");
        String sessionId = headerAccessor.getSessionId();
        System.out.println("ip="+ip+",sessionId="+sessionId);
        System.out.println(chatMessageDTO);
        System.out.println(headerAccessor);
        messagingTemplate.convertAndSend("/topic/chat/"+chatMessageDTO.getSender(),chatMessageDTO);
        return chatMessageDTO;
    }

    @Override
    public ChatMessageDTO addUser(ChatMessageDTO chatMessageDTO, SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("addUser Service  ... ");
        String ip = (String) headerAccessor.getSessionAttributes().get("ip");
        chatMessageDTO.setIp(ip);
        System.out.println(chatMessageDTO);
        return chatMessageDTO;
    }


}
