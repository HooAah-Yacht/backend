package HooYah.Yacht.chat.domain;

import HooYah.Yacht.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "chat_conversation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatConversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, unique = true)
    private String conversationId;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages;

    public static ChatConversation create(User user, String conversationId) {
        OffsetDateTime now = OffsetDateTime.now();
        return ChatConversation.builder()
                .user(user)
                .conversationId(conversationId)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public void updateTimestamp() {
        this.updatedAt = OffsetDateTime.now();
    }
}

