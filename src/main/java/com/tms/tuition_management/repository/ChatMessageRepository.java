package com.tms.tuition_management.repository;

import com.tms.tuition_management.model.ChatMessage;
import com.tms.tuition_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findBySenderAndRecipientOrRecipientAndSenderOrderByTimestampAsc(User user1, User user1Again, User user2, User user2Again);
    List<ChatMessage> findBySenderOrRecipientOrderByTimestampDesc(User sender, User recipient);
}