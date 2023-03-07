package com.lwdevelop.customerServiceAdmin.dto;

import lombok.Data;

@Data
public class ChatMessageDTO {

    private String uniqueId;

    private String ip;

    // for adminId
    private Long toAdminId;
    
    private String content;

    private Long timestamp;



    public ChatMessageDTO() {}

    public ChatMessageDTO(String uniqueId,String ip,Long toAdminId,String content, Long timestamp) {
        this.uniqueId = uniqueId;
        this.ip = ip;
        this.toAdminId = toAdminId;
        this.content = content;
        this.timestamp = timestamp;
    }

}
