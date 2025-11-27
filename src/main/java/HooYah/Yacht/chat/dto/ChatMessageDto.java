package HooYah.Yacht.chat.dto;

import HooYah.Yacht.chat.domain.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ChatMessageDto {
    
    private String content;
    private ChatMessage.MessageRole role;
    private OffsetDateTime createdAt;
    
    public static ChatMessageDto from(ChatMessage message) {
        return ChatMessageDto.builder()
                .content(message.getContent())
                .role(message.getRole())
                .createdAt(message.getCreatedAt())
                .build();
    }
}

