package HooYah.Yacht.chat.repository;

import HooYah.Yacht.chat.domain.ChatConversation;
import HooYah.Yacht.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatConversationRepository extends JpaRepository<ChatConversation, Long> {
    
    Optional<ChatConversation> findByUser(User user);
    
    Optional<ChatConversation> findByConversationId(String conversationId);
}

