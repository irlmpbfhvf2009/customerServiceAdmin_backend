package com.lwdevelop.customerServiceAdmin.dto;

import lombok.Data;

@Data
public class UserDTO {
    
    private String ip;
    private String email;
    
    // constructors, getters and setters
    
    public UserDTO(String ip,String email) {
        this.ip = ip;
        this.email = email;
    }
}
