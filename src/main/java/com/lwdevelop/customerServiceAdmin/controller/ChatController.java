package com.lwdevelop.customerServiceAdmin.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.lwdevelop.customerServiceAdmin.dto.ChatMessageDTO;

@Controller
@RequestMapping("/tmax/ws")
public class ChatController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 发送聊天消息
     *
     * @param chatMessage 聊天消息对象
     * @return 发送结果
     */
    @MessageMapping("/topic/chat/{uniqueId}")
    public ChatMessageDTO handleMessage(@Payload ChatMessageDTO message, SimpMessageHeaderAccessor headerAccessor) {

        String ip = (String) headerAccessor.getSessionAttributes().get("ip");
        String sessionId = headerAccessor.getSessionId();
        System.out.println("ip="+ip+",sessionId="+sessionId);
        System.out.println(message);
        System.out.println(headerAccessor);

        // ChatMessage類表示收到的消息，SimpMessageHeaderAccessor可以獲取消息頭信息
        messagingTemplate.convertAndSend("/topic/chat/"+message.getUniqueId(),message);
        // rabbitTemplate.convertAndSend("chat-exchange", "tmax/ws", message.getContent());
        // System.out.println(headerAccessor);
        return message;
    }
}
