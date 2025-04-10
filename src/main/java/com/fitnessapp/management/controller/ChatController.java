package com.fitnessapp.management.controller;

import com.fitnessapp.management.repository.dto.MessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public MessageDTO sendMessage(MessageDTO messageDTO) {
        return messageDTO;
    }
}