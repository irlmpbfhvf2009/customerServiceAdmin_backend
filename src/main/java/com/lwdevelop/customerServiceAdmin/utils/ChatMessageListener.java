package com.lwdevelop.customerServiceAdmin.utils;

import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

@Component
public class ChatMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        String text = new String(message.getBody());
        System.out.println("Received message: " + text);
        // 处理接收到的消息
    }

}
