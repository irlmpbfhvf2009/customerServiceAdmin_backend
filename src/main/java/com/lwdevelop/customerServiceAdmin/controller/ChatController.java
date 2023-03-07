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
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 发送聊天消息
     *
     * @param chatMessage 聊天消息对象
     * @return 发送结果
     */
    @MessageMapping("/topic/chat/{uniqueId}")
    public ChatMessageDTO handleMessage(@Payload ChatMessageDTO chatMessageDTO, SimpMessageHeaderAccessor headerAccessor) {

        String ip = (String) headerAccessor.getSessionAttributes().get("ip");
        String sessionId = headerAccessor.getSessionId();
        System.out.println("ip="+ip+",sessionId="+sessionId);
        System.out.println(chatMessageDTO);
        System.out.println(headerAccessor);
        messagingTemplate.convertAndSend("/topic/chat/"+chatMessageDTO.getUniqueId(),chatMessageDTO);
        return chatMessageDTO;
    }
    // add user in web socket session
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessageDTO addUser(@Payload ChatMessageDTO chatMessageDTO,
                                  SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessageDTO.getSender());
        return chatMessageDTO;
    }

    // send message to user
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDTO chatMessageDTO) {
        messagingTemplate.convertAndSendToUser(chatMessageDTO.getReceiver(), "/queue/messages",
                chatMessageDTO);
    }

    // send message to user
    @MessageMapping("/chat.sendMessageToAll")
    public void sendMessageToAll(@Payload ChatMessageDTO chatMessageDTO) {
        messagingTemplate.convertAndSend("/topic/chat/"+chatMessageDTO.getUniqueId(),chatMessageDTO);
    }


}
