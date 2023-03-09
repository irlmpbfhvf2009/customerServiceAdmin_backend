package com.lwdevelop.customerServiceAdmin.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoadCustomService {

    private Map<String, ChatMessageDTO> onlineUsers;
    private Map<String, ChatMessageDTO> onlineCustomService;
}
