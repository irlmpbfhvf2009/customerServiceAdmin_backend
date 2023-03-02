package com.lwdevelop.customerServiceAdmin.dto;

import lombok.Data;

@Data
public class ChatMessageDTO {

    private Long id;

    private UserDTO user;

    private AdminDTO admin;

    private String message;

    private String senderEmail;


    public ChatMessageDTO() {}

    public ChatMessageDTO(UserDTO user, AdminDTO admin, String message, String senderEmail) {
        this.user = user;
        this.admin = admin;
        this.message = message;
        this.senderEmail = senderEmail;
    }

    // getters and setters
}
