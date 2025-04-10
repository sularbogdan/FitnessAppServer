package com.fitnessapp.management.service.implementation;

import com.fitnessapp.management.repository.MessageRepository;
import com.fitnessapp.management.repository.UserRepository;
import com.fitnessapp.management.repository.entity.Message;
import com.fitnessapp.management.repository.entity.User;
import com.fitnessapp.management.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;
    private UserRepository userRepository;

    public MessageServiceImpl(MessageRepository messageRepository, UserRepository userRepository){
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Message> getMessagesBetweenUsersAndAdmin(Long senderId, Long receiverId) {
        return messageRepository.findBySenderIdAndReceiverId(senderId, receiverId);
    }

    @Override
    public Message sendMessage(Long senderId, Long receiverId, String content) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("Receiver not found"));

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @Override
    public List<User> getUsersUserChattedWith(Long userId) {
        List<Message> messages = messageRepository.findAllMessagesByUser(userId);

        return messages.stream()
                .map(msg -> {
                    if (msg.getSender().getId().equals(userId)) {
                        return msg.getReceiver();
                    } else {
                        return msg.getSender();
                    }
                })
                .distinct()
                .toList();
    }

}
