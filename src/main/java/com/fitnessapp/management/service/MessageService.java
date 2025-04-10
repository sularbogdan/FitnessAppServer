package com.fitnessapp.management.service;

import com.fitnessapp.management.repository.entity.Message;

import java.util.List;

public interface MessageService {
    List<Message> getMessagesBetweenUsersAndAdmin(Long senderId, Long receiverId);
    Message sendMessage(Long senderId, Long receiverId, String content);
}
