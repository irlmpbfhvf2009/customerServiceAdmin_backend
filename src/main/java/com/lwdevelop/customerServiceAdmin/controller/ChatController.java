package com.lwdevelop.customerServiceAdmin.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
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
    @MessageMapping("/chat.userOnlineStatus")
    @SendTo("/topic/userOnlineStatus")
    public List<ChatMessageDTO> userOnlineStatus(@Payload ChatMessageDTO chatMessageDTO,SimpMessageHeaderAccessor headerAccessor) {

        return chatMessageService.userOnlineStatus(chatMessageDTO, headerAccessor);
    }


    // send message to user
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDTO chatMessageDTO) {

        chatMessageService.sendMessage(chatMessageDTO);
    }


}
