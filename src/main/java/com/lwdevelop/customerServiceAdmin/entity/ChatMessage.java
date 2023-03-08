package com.lwdevelop.customerServiceAdmin.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 傳送者
    private String sender;

    // 接收者
    private String receiver;

    private String ip;

    private String content;

    private Date timestamp;

    public ChatMessage(String sender, String receiver, String ip, String content, Date timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.ip = ip;
        this.content = content;
        this.timestamp = timestamp;
    }

}
