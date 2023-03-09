package com.lwdevelop.customerServiceAdmin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lwdevelop.customerServiceAdmin.dto.ChatMessageDTO;
import com.lwdevelop.customerServiceAdmin.service.impl.ChatMessageServiceImpl;

@RestController
@RequestMapping("/tmax/ws")
public class ChatController {
    
    @Autowired
    private ChatMessageServiceImpl chatMessageService;


    // user join or leave in web socket session
    @MessageMapping("/chat.userUpdate")
    @SendTo("/topic/userUpdate")
    public ChatMessageDTO userUpdate(@Payload ChatMessageDTO chatMessageDTO,SimpMessageHeaderAccessor headerAccessor) {

        return chatMessageService.userUpdate(chatMessageDTO, headerAccessor);
    }

    // send message to user
    @MessageMapping("/chat.sendMessage")
    public void sendMessageToUser(@Payload ChatMessageDTO chatMessageDTO) {

        chatMessageService.sendMessageToUser(chatMessageDTO);
    }

    // 客服載入onlineCustomService
    @MessageMapping("/chat.loadCustomService")
    @SendTo("/topic/loadCustomService")
    public void loadCustomService() {
        
        chatMessageService.loadCustomService();
    }

}
