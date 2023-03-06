package com.lwdevelop.customerServiceAdmin.dto;

import lombok.Data;

@Data
public class ChatMessageDTO {

    private String uniqueId;

    private String message;



    public ChatMessageDTO() {}

    public ChatMessageDTO(String uniqueId,String message) {
        this.uniqueId = uniqueId;
        this.message = message;
    }

    // getters and setters
}
