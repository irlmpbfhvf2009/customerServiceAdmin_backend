package com.lwdevelop.customerServiceAdmin.controller;


import javax.websocket.Session;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.lwdevelop.customerServiceAdmin.dto.ChatMessageDTO;
import com.lwdevelop.customerServiceAdmin.entity.ChatMessage;
import com.lwdevelop.customerServiceAdmin.service.impl.ChatMessageServiceImpl;

@Controller
@RequestMapping("/tmax/ws")
public class ChatController {

    // @Autowired
    // private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ChatMessageServiceImpl chatMessageServiceImpl;

    /**
     * 发送聊天消息
     *
     * @param chatMessage 聊天消息对象
     * @return 发送结果
     */
    @MessageMapping("/topic/chat/{uniqueId}")
    @SendTo("/topic/chat/{uniqueId}")
    public ChatMessageDTO handleMessage(@Payload ChatMessageDTO message, SimpMessageHeaderAccessor headerAccessor) {
        System.out.println(message);
        // 在這裡處理消息
        // ChatMessage類表示收到的消息，SimpMessageHeaderAccessor可以獲取消息頭信息
        // rabbitTemplate.convertAndSend("chat-exchange","tmax/ws", message.getMessage());
        // System.out.println(headerAccessor);
        // System.out.println(message);
        // System.out.println("111");
        // System.out.println(message.getMessage());
        return message;
    }


    /**
     * 加入房间
     *
     * @param chatMessage 聊天消息对象
     * @param session     WebSocket 会话
     */
    // @MessageMapping("/chat.addUser")
    // public void addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor, Session session) {
    //     headerAccessor.getSessionAttributes().put("username", chatMessage.getFromUser());
    //     headerAccessor.getSessionAttributes().put("roomId", chatMessage.getRoomId());

    //     // 发送系统通知，告知所有用户有新用户加入了房间
    //     messagingTemplate.convertAndSend("/topic/room/" + chatMessage.getRoomId(), chatMessage);
    // }

    /**
     * 接收然后转发至客户端消息
     * 
     * @param message
     * @return
     * @throws Exception
     */
    @MessageMapping("/top")
    public String greeting(String message) throws Exception {
        System.out.println("receiving " + message);
        System.out.println("connecting successfully.");
        return "AAA:" + message;
    }

    /**
     * 推送消息
     * 
     * @return
     */
    // @ResponseBody
    // @RequestMapping("/hello/addMessage")
    // public ResponseEntity<String> addMessage(@RequestParam("id") String id) {
    //     messagingTemplate.convertAndSendToUser(id, "/topic/greetings", "您收到了新的系统消息");
    //     return ResponseEntity.status(HttpStatus.OK).body("success");
    // }
}
