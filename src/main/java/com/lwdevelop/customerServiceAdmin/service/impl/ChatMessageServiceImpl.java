package com.lwdevelop.customerServiceAdmin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.lwdevelop.customerServiceAdmin.dto.ChatMessageDTO;
import com.lwdevelop.customerServiceAdmin.dto.LoadCustomService;
import com.lwdevelop.customerServiceAdmin.entity.ChatMessage;
import com.lwdevelop.customerServiceAdmin.repository.ChatMessageRepository;
import com.lwdevelop.customerServiceAdmin.service.ChatMessageService;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    
    

    @Override
    public void saveMessage(ChatMessageDTO chatMessageDTO) {
        throw new UnsupportedOperationException("Unimplemented method 'saveMessage'");
    }



    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

    
    // 線上用戶列表
    private static Map<String, ChatMessageDTO> onlineUsers = new HashMap<>();
    // 線上客服列表
    private static Map<String, ChatMessageDTO> onlineCustomService = new HashMap<>();
    
    @Override
    public void sendMessageToUser(ChatMessageDTO chatMessageDTO) {
        // client { sender: 玩家, receiver: admin, isUser:true  , content : 123 }
        // server { sender: 客服, receiver: 玩家 , isUser:false , content : 123  }
        String receiver = chatMessageDTO.getReceiver();
        System.out.println("Service sendMessageToUser ... receiver:"+receiver);
        if(receiver != null){
            MessageProperties props = new MessageProperties();
            props.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            byte[] b = (chatMessageDTO.getSender() + "," + chatMessageDTO.getContent()).getBytes();
            Message msg = new Message(b, props);
            System.out.println("Service sendMessageToUser ... receiver != null");

            if(chatMessageDTO.getIsUser()){
                System.out.println("Service sendMessageToUser ...  chatMessageDTO.getIsUser()");
                if(onlineCustomService.containsKey(receiver)){
                    System.out.println("Service sendMessageToUser ...  onlineCustomService.containsKey(receiver)");
                    System.out.println("receiver"+receiver);
                    messagingTemplate.convertAndSendToUser(receiver, "/queue/messages", chatMessageDTO);
                }else{
                    System.out.println("Service sendMessageToUser ...  !onlineCustomService.containsKey(receiver)");
                    rabbitTemplate.convertAndSend("offline.message.exchange", "user." + receiver, msg);
                }
            }else{
                System.out.println("Service sendMessageToUser ...  !chatMessageDTO.getIsUser()");
                if(onlineUsers.containsKey(receiver)){
                    System.out.println("Service sendMessageToUser ...  onlineUsers.containsKey(receiver)");
                    messagingTemplate.convertAndSendToUser(receiver, "/queue/messages", chatMessageDTO);
                }else{
                    System.out.println("Service sendMessageToUser ...  !onlineUsers.containsKey(receiver)");
                    rabbitTemplate.convertAndSend("offline.message.exchange", "user." + receiver, msg);
                }
            }
            
            
        }
    }


    @Override
    public ChatMessageDTO userUpdate(ChatMessageDTO chatMessageDTO, SimpMessageHeaderAccessor headerAccessor) {

        if(chatMessageDTO.getType().equals("JOIN")){
            String ip = (String) headerAccessor.getSessionAttributes().get("ip");
            chatMessageDTO.setIp(ip);
            if(chatMessageDTO.getIsUser()){
                onlineUsers.put(chatMessageDTO.getSender(), chatMessageDTO);
                if(onlineCustomService.size() > 0){
                    List<String> keys = new ArrayList<>(onlineCustomService.keySet());
                    String randomKey = keys.get(new Random().nextInt(keys.size()));
                    chatMessageDTO.setReceiver(randomKey);
                }
            }else{
                onlineCustomService.put(chatMessageDTO.getSender(), chatMessageDTO);
            }
            return chatMessageDTO;
        }else{
            if(chatMessageDTO.getIsUser()){
                onlineUsers.remove(chatMessageDTO.getSender());
            }else{
                onlineCustomService.remove(chatMessageDTO.getSender());
            }
            return chatMessageDTO;
        }
    }



    @Override
    public void loadCustomService() {
        LoadCustomService loadCustomService = new LoadCustomService();
        loadCustomService.setOnlineUsers(onlineUsers);
        loadCustomService.setOnlineCustomService(onlineCustomService);
    }




}
