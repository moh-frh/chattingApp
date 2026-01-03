package com.chatapp.service;

import com.chatapp.dto.ChatMessageRequest;
import com.chatapp.dto.ChatMessageResponse;
import com.chatapp.model.ChatMessage;
import com.chatapp.model.User;
import com.chatapp.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class ChatMessageService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserService userService;

    public ChatMessageResponse sendMessage(Long senderId, ChatMessageRequest request) {
        User sender = userService.findUserById(senderId);
        User receiver = userService.findUserById(request.getReceiverId());

        ChatMessage message = new ChatMessage();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(request.getContent());
        message.setIsRead(false);

        ChatMessage savedMessage = chatMessageRepository.save(message);
        return convertToResponse(savedMessage);
    }

    public List<ChatMessageResponse> getConversation(Long userId1, Long userId2) {
        User user1 = userService.findUserById(userId1);
        User user2 = userService.findUserById(userId2);

        List<ChatMessage> messages = chatMessageRepository.findConversationBetweenUsers(user1, user2);
        return messages.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ChatMessageResponse> getUnreadMessages(Long userId) {
        User user = userService.findUserById(userId);
        List<ChatMessage> messages = chatMessageRepository.findUnreadMessagesForUser(user);
        return messages.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public void markAsRead(Long messageId) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.setIsRead(true);
        chatMessageRepository.save(message);
    }

    public void markConversationAsRead(Long userId1, Long userId2) {
        User user1 = userService.findUserById(userId1);
        User user2 = userService.findUserById(userId2);
        
        List<ChatMessage> unreadMessages = chatMessageRepository.findConversationBetweenUsers(user1, user2)
                .stream()
                .filter(msg -> msg.getReceiver().getId().equals(userId1) && !msg.getIsRead())
                .collect(java.util.stream.Collectors.toList());
        
        for (ChatMessage message : unreadMessages) {
            message.setIsRead(true);
            chatMessageRepository.save(message);
        }
    }

    public Long getUnreadCount(Long userId, Long senderId) {
        User user = userService.findUserById(userId);
        User sender = userService.findUserById(senderId);
        List<ChatMessage> unreadMessages = chatMessageRepository.findUnreadMessagesForUser(user);
        return unreadMessages.stream()
                .filter(msg -> msg.getSender().getId().equals(senderId))
                .count();
    }

    private ChatMessageResponse convertToResponse(ChatMessage message) {
        ChatMessageResponse response = new ChatMessageResponse();
        response.setId(message.getId());
        response.setSenderId(message.getSender().getId());
        response.setSenderUsername(message.getSender().getUsername());
        response.setReceiverId(message.getReceiver().getId());
        response.setReceiverUsername(message.getReceiver().getUsername());
        response.setContent(message.getContent());
        response.setCreatedAt(message.getCreatedAt());
        response.setIsRead(message.getIsRead());
        return response;
    }
}

