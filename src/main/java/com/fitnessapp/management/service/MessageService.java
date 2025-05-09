package com.fitnessapp.management.service;

import com.fitnessapp.management.repository.entity.Message;
import com.fitnessapp.management.repository.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageService {
    List<Message> getMessagesBetweenUsers(Long userId1, Long userId2);
    Message sendMessage(Long senderId, Long receiverId, String content);
    List<User> getUsersUserChattedWith(Long userId);
}
