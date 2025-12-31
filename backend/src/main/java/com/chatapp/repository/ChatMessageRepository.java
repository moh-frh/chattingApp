package com.chatapp.repository;

import com.chatapp.model.ChatMessage;
import com.chatapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderAndReceiverOrderByCreatedAtAsc(User sender, User receiver);
    List<ChatMessage> findByReceiverAndSenderOrderByCreatedAtAsc(User receiver, User sender);
    
    @Query("SELECT m FROM ChatMessage m WHERE " +
           "(m.sender = :user1 AND m.receiver = :user2) OR " +
           "(m.sender = :user2 AND m.receiver = :user1) " +
           "ORDER BY m.createdAt ASC")
    List<ChatMessage> findConversationBetweenUsers(@Param("user1") User user1, @Param("user2") User user2);
    
    @Query("SELECT m FROM ChatMessage m WHERE m.receiver = :user AND m.isRead = false")
    List<ChatMessage> findUnreadMessagesForUser(@Param("user") User user);
}


