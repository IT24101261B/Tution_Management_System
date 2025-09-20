package com.tms.tuition_management.service;

import com.tms.tuition_management.model.ChatMessage;
import com.tms.tuition_management.model.User;
import com.tms.tuition_management.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;

    public ChatService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public List<ChatMessage> getConversation(User user1, User user2) {
        return chatMessageRepository.findBySenderAndRecipientOrRecipientAndSenderOrderByTimestampAsc(user1, user1, user2, user2);
    }

    public void sendMessage(User sender, User recipient, String content) {
        ChatMessage message = new ChatMessage();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        chatMessageRepository.save(message);
    }

    public Map<User, ChatMessage> getConversationsForUser(User user) {
        List<ChatMessage> messages = chatMessageRepository.findBySenderOrRecipientOrderByTimestampDesc(user, user);
        Map<User, ChatMessage> conversations = new LinkedHashMap<>();

        for (ChatMessage message : messages) {
            User otherUser = message.getSender().equals(user) ? message.getRecipient() : message.getSender();
            conversations.putIfAbsent(otherUser, message);
        }
        return conversations;
    }
}