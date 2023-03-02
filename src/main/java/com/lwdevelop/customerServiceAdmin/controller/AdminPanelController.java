package com.lwdevelop.customerServiceAdmin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.lwdevelop.customerServiceAdmin.entity.ChatMessage;

@Controller
@RequestMapping("/admin")
public class AdminPanelController {
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/admin_panel")
    public String adminPanel(Model model) {
        // List<User> users = userService.getAllUsers();
        // model.addAttribute("users", users);
        return "admin_panel";
    }

    @PostMapping("/send-message")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        System.out.println("Message received: " + chatMessage);
        messagingTemplate.convertAndSend("/topic/messages", chatMessage);
    }
    
}
