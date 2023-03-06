package com.lwdevelop.customerServiceAdmin.controller;


import java.util.List;

import javax.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.lwdevelop.customerServiceAdmin.entity.ChatMessage;
import com.lwdevelop.customerServiceAdmin.service.impl.ChatMessageServiceImpl;

@Controller
@RequestMapping("/tmax/ws")
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatMessageServiceImpl chatMessageServiceImpl;

    /**
     * 发送聊天消息
     *
     * @param chatMessage 聊天消息对象
     * @return 发送结果
     */
    @GetMapping("/api/chat")
    public List<ChatMessage> getChatMessages(@Payload ChatMessage chatMessage) {
        return null;
        // ...
    }
    /**
     * 发送聊天消息
     *
     * @param chatMessage 聊天消息对象
     * @return 发送结果
     */
    @MessageMapping("/chat.sendMessage")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        // 将聊天消息保存到数据库中
        chatMessageServiceImpl.save(chatMessage);

        // 将聊天消息发送给所有订阅了相应房间的客户端
        messagingTemplate.convertAndSend("/topic/room/" + chatMessage.getRoomId(), chatMessage);

        return chatMessage;
    }

    /**
     * 加入房间
     *
     * @param chatMessage 聊天消息对象
     * @param session     WebSocket 会话
     */
    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor, Session session) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getFromUser());
        headerAccessor.getSessionAttributes().put("roomId", chatMessage.getRoomId());

        // 发送系统通知，告知所有用户有新用户加入了房间
        messagingTemplate.convertAndSend("/topic/room/" + chatMessage.getRoomId(), chatMessage);
    }

    /**
     * 接收然后转发至客户端消息
     * 
     * @param message
     * @return
     * @throws Exception
     */
    @MessageMapping("/top")
    @SendTo("/topic/greetings")
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
    @ResponseBody
    @RequestMapping("/hello/addMessage")
    public ResponseEntity<String> addMessage(@RequestParam("id") String id) {
        messagingTemplate.convertAndSendToUser(id, "/topic/greetings", "您收到了新的系统消息");
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }
}
