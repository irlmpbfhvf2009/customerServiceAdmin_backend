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

    private String uniqueId;

    private String ip;

    private Long toAdminId;

    private String content;

    private Long timestamp;



    public ChatMessage(String uniqueId, String ip, Long toAdminId, String content, Long timestamp) {
        this.uniqueId = uniqueId;
        this.ip = ip;
        this.toAdminId = toAdminId;
        this.content = content;
        this.timestamp = timestamp;
    }
    
}
