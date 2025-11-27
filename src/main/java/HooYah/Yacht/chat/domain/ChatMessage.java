package HooYah.Yacht.chat.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "chat_message")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private ChatConversation conversation;

    @Column(nullable = false, length = 250)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageRole role;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    public enum MessageRole {
        SYSTEM, USER, ASSISTANT
    }

    public static ChatMessage create(ChatConversation conversation, String content, MessageRole role) {
        return ChatMessage.builder()
                .conversation(conversation)
                .content(content)
                .role(role)
                .createdAt(OffsetDateTime.now())
                .build();
    }
}

