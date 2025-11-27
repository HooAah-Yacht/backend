package HooYah.Yacht.chat.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ChatRequest {
    
    @NotEmpty
    private String message;
    @Deprecated
    private String conversationId;
}

