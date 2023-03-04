package com.lwdevelop.customerServiceAdmin.config;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwdevelop.customerServiceAdmin.entity.ChatMessage;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Component
public class ChatMessageListener implements MessageListener {

    private SimpMessagingTemplate messagingTemplate;
    
    @Override
    public void onMessage(Message message) {
        String payload = new String(message.getBody());
        // 根据消息的类型进行转换，这里假设消息是JSON格式的
        ChatMessage chatMessage;
        try {
            chatMessage = convertMessage(payload);
            messagingTemplate.convertAndSend("/topic/chat/" + chatMessage.getRoomId(), chatMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // 将消息发送给指定的WebSocket客户端
    }
    private ChatMessage convertMessage(String payload) throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        return chatMessage;
    }
    
}
