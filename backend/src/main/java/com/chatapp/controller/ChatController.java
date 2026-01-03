package com.chatapp.controller;

import com.chatapp.dto.ChatMessageRequest;
import com.chatapp.dto.ChatMessageResponse;
import com.chatapp.service.ChatMessageService;
import com.chatapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private UserService userService;

    @PostMapping("/send")
    public ResponseEntity<ChatMessageResponse> sendMessage(@Valid @RequestBody ChatMessageRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            com.chatapp.model.User sender = userService.findByUsername(username);
            
            ChatMessageResponse response = chatMessageService.sendMessage(sender.getId(), request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/conversation/{userId}")
    public ResponseEntity<List<ChatMessageResponse>> getConversation(@PathVariable Long userId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            com.chatapp.model.User currentUser = userService.findByUsername(username);
            
            List<ChatMessageResponse> messages = chatMessageService.getConversation(currentUser.getId(), userId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/unread")
    public ResponseEntity<List<ChatMessageResponse>> getUnreadMessages() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            com.chatapp.model.User user = userService.findByUsername(username);
            
            List<ChatMessageResponse> messages = chatMessageService.getUnreadMessages(user.getId());
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/read/{messageId}")
    public ResponseEntity<Void> markAsRead(@PathVariable Long messageId) {
        try {
            chatMessageService.markAsRead(messageId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/read-conversation/{userId}")
    public ResponseEntity<Void> markConversationAsRead(@PathVariable Long userId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            com.chatapp.model.User currentUser = userService.findByUsername(username);
            
            chatMessageService.markConversationAsRead(currentUser.getId(), userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/unread-count/{userId}")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long userId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            com.chatapp.model.User currentUser = userService.findByUsername(username);
            
            Long count = chatMessageService.getUnreadCount(currentUser.getId(), userId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.ok(0L);
        }
    }
}


