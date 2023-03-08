package com.lwdevelop.customerServiceAdmin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lwdevelop.customerServiceAdmin.dto.ChatMessageDTO;
import com.lwdevelop.customerServiceAdmin.service.impl.ChatMessageServiceImpl;

@RestController
@RequestMapping("/tmax/ws")
public class ChatController {
    
    @Autowired
    private ChatMessageServiceImpl chatMessageService;

    /**
     * 发送聊天消息
     *
     * @param chatMessage 聊天消息对象
     * @return 发送结果
     */
    @MessageMapping("/topic/chat/{uniqueId}")
    public ChatMessageDTO handleMessage(@Payload ChatMessageDTO chatMessageDTO, SimpMessageHeaderAccessor headerAccessor) {

        return chatMessageService.handleMessage(chatMessageDTO, headerAccessor);
    }
    // user join or leave in web socket session
    @MessageMapping("/chat.userUpdate")
    @SendTo("/topic/userUpdate")
    public ChatMessageDTO userUpdate(@Payload ChatMessageDTO chatMessageDTO,SimpMessageHeaderAccessor headerAccessor) {

        return chatMessageService.userUpdate(chatMessageDTO, headerAccessor);
    }

    // 用戶指派隨機客服(設定receiver)
    @MessageMapping("/chat.assignUser")
    @SendTo("/topic/assignUser")
    public ChatMessageDTO assignUser(@Payload ChatMessageDTO chatMessageDTO) {

        return chatMessageService.assignUser(chatMessageDTO);
    }

    // send message to user
    @MessageMapping("/chat.sendMessage")
    public void sendMessageToUser(@Payload ChatMessageDTO chatMessageDTO) {

        chatMessageService.sendMessageToUser(chatMessageDTO);
    }

}
