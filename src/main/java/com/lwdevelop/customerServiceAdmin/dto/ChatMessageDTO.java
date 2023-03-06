package com.lwdevelop.customerServiceAdmin.dto;

import lombok.Data;

@Data
public class ChatMessageDTO {

    private String uniqueId;

    private String content;



    public ChatMessageDTO() {}

    public ChatMessageDTO(String uniqueId,String content) {
        this.uniqueId = uniqueId;
        this.content = content;
    }

}
