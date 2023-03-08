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
    // add user in web socket session
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessageDTO addUser(@Payload ChatMessageDTO chatMessageDTO,SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("Controller addUser  ... ");
        System.out.println(chatMessageDTO);
        System.out.println(headerAccessor);

        return chatMessageService.addUser(chatMessageDTO, headerAccessor);
    }

    // send message to user
    @MessageMapping("/chat.sendMessage")
    public void sendMessageToUser(@Payload ChatMessageDTO chatMessageDTO) {

        chatMessageService.sendMessageToUser(chatMessageDTO);
    }

}
