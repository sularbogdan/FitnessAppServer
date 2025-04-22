package com.fitnessapp.management.controller;

import com.fitnessapp.management.repository.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void send(MessageDTO message) {

        messagingTemplate.convertAndSend("/topic/chat/" + message.getReceiverId(), message);
        messagingTemplate.convertAndSend("/topic/chat/" + message.getSenderId(), message);
    }
}