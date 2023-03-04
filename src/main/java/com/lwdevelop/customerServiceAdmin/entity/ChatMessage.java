package com.lwdevelop.customerServiceAdmin.entity;

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

    private String fromUser;

    private String toUser;

    private String content;

    private Long timestamp;

    private Long roomId; // 添加 roomId 字段

    public ChatMessage(String fromUser, String toUser, String content, Long timestamp, Long roomId) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.content = content;
        this.timestamp = timestamp;
        this.roomId = roomId;
    }
    
}
