package com.fitnessapp.management.controller;

import com.fitnessapp.management.repository.dto.MessageDTO;
import com.fitnessapp.management.repository.entity.Message;
import com.fitnessapp.management.repository.entity.User;
import com.fitnessapp.management.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/{senderId}/{receiverId}")
    public List<MessageDTO> getMessages(@PathVariable Long senderId, @PathVariable Long receiverId) {
        return messageService.getMessagesBetweenUsers(senderId, receiverId).stream().map(msg -> {
            MessageDTO dto = new MessageDTO();
            dto.setSenderId(msg.getSender().getId());
            dto.setReceiverId(msg.getReceiver().getId());
            dto.setContent(msg.getContent());
            dto.setTimestamp(msg.getTimestamp());
            return dto;
        }).collect(Collectors.toList());
    }

    @PostMapping("/send")
    public MessageDTO sendMessage(@RequestBody MessageDTO messageDTO) {
        Message message = messageService.sendMessage(messageDTO.getSenderId(), messageDTO.getReceiverId(), messageDTO.getContent());
        messageDTO.setTimestamp(message.getTimestamp());
        return messageDTO;
    }
}