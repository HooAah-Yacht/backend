package HooYah.Yacht.chat.repository;

import HooYah.Yacht.chat.domain.ChatMessage;
import HooYah.Yacht.chat.domain.ChatConversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    List<ChatMessage> findByConversationOrderByCreatedAtAsc(ChatConversation conversation);
}

