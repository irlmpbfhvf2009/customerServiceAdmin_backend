package com.lwdevelop.customerServiceAdmin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/test")
public class testController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 跳转至hello.html界面
     * @return
     */
    @RequestMapping("/hello")
    public String hello(){
        return "test";
    }

    /**
     * 接收然后转发至客户端消息
     * @param message
     * @return
     * @throws Exception
     */
    @MessageMapping("/top")
    @SendTo("/topic/greetings")
    public String greeting(String message) throws Exception {
        System.out.println("receiving " + message);
        System.out.println("connecting successfully.");
        return "AAA:"+message;
    }
    

    /**
     * 推送消息
     * @return
     */
    @ResponseBody
    @RequestMapping("/hello/addMessage")
    public ResponseEntity<String> addMessage(@RequestParam("id") String id){
        messagingTemplate.convertAndSendToUser(id, "/topic/greetings" ,"您收到了新的系统消息");
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }
}
