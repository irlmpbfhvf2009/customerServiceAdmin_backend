package com.lwdevelop.customerServiceAdmin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatMessageServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

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

    @Override
    public void sendMessageToUser(ChatMessageDTO chatMessageDTO) {
        messagingTemplate.convertAndSendToUser(chatMessageDTO.getReceiver(), "/queue/messages",
        chatMessageDTO);
    }

    // 線上用戶列表
    private static Map<String, ChatMessageDTO> onlineUsers = new HashMap<>();
    // 線上客服列表
    private static Map<String, ChatMessageDTO> onlineCustomService = new HashMap<>();
    
    @Override
    public ChatMessageDTO handleMessage(ChatMessageDTO chatMessageDTO, SimpMessageHeaderAccessor headerAccessor) {
        String ip = (String) headerAccessor.getSessionAttributes().get("ip");
        String sessionId = headerAccessor.getSessionId();
        return chatMessageDTO;
    }

    @Override
    public ChatMessageDTO userUpdate(ChatMessageDTO chatMessageDTO, SimpMessageHeaderAccessor headerAccessor) {

        if(chatMessageDTO.getType().equals("JOIN")){
            String ip = (String) headerAccessor.getSessionAttributes().get("ip");
            chatMessageDTO.setIp(ip);
            if(chatMessageDTO.getIsUser()){
                onlineUsers.put(chatMessageDTO.getSender(), chatMessageDTO);
                if(onlineCustomService.size() > 0){
                    List<String> keys = new ArrayList<>(onlineCustomService.keySet());
                    String randomKey = keys.get(new Random().nextInt(keys.size()));
                    chatMessageDTO.setReceiver(randomKey);
                }
            }else{
                onlineCustomService.put(chatMessageDTO.getSender(), chatMessageDTO);
            }
            return chatMessageDTO;
        }else{
            if(chatMessageDTO.getIsUser()){
                onlineUsers.remove(chatMessageDTO.getSender());
            }else{
                onlineCustomService.remove(chatMessageDTO.getSender());
            }
            return chatMessageDTO;
        }
    }


    // 用戶指派隨機客服(設定receiver)
    @Override
    public ChatMessageDTO assignUser(ChatMessageDTO chatMessageDTO) {
        if(onlineCustomService.size() > 0){
            List<String> keys = new ArrayList<>(onlineCustomService.keySet());
            String randomKey = keys.get(new Random().nextInt(keys.size()));
            chatMessageDTO.setReceiver(randomKey);
        }
        return chatMessageDTO;
    }



}
