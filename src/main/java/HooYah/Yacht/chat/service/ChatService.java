package HooYah.Yacht.chat.service;

import HooYah.Yacht.chat.domain.ChatConversation;
import HooYah.Yacht.chat.domain.ChatMessage;
import HooYah.Yacht.chat.dto.ChatMessageDto;
import HooYah.Yacht.chat.dto.ChatResponse;
import HooYah.Yacht.chat.repository.ChatConversationRepository;
import HooYah.Yacht.chat.repository.ChatMessageRepository;
import HooYah.Yacht.chat.util.ChatPromptFactory;
import HooYah.Yacht.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatModel chatModel;
    private final ChatPromptFactory promptFactory;
    private final ChatConversationRepository conversationRepository;
    private final ChatMessageRepository messageRepository;

    @Transactional
    public ChatResponse sendMessage(User user, String message) {
        // 유저의 conversation 찾기 또는 생성
        ChatConversation conversation = conversationRepository.findByUser(user)
                .orElseGet(() -> {
                    String conversationId = UUID.randomUUID().toString();
                    return conversationRepository.save(ChatConversation.create(user, conversationId));
                });

        // 이전 대화 히스토리 불러오기
        List<ChatMessage> previousMessages = messageRepository.findByConversationOrderByCreatedAtAsc(conversation);
        
        List<Message> messages = new ArrayList<>();
        
        if (previousMessages.isEmpty()) {
            messages.add(new SystemMessage(promptFactory.createSystemPrompt()));
        }
        
        // 이전 대화 히스토리 추가
        for (ChatMessage chatMessage : previousMessages) {
            if (chatMessage.getRole() == ChatMessage.MessageRole.USER) {
                messages.add(new UserMessage(chatMessage.getContent()));
            } else if (chatMessage.getRole() == ChatMessage.MessageRole.ASSISTANT) {
                messages.add(new AssistantMessage(chatMessage.getContent()));
            }
        }
        
        messages.add(new UserMessage(message));
        
        // AI 호출
        Prompt prompt = new Prompt(messages);
        org.springframework.ai.chat.model.ChatResponse aiResponse = chatModel.call(prompt);
        String responseText = aiResponse.getResult().getOutput().getText();
        
        String truncatedMessage = truncateTo250Chars(message);
        String truncatedResponse = truncateTo250Chars(responseText);
        
        // 사용자 메시지 저장 (질문)
        ChatMessage userMessage = ChatMessage.create(conversation, truncatedMessage, ChatMessage.MessageRole.USER);
        messageRepository.save(userMessage);
        
        // AI 응답 저장 (답변)
        ChatMessage assistantMessage = ChatMessage.create(conversation, truncatedResponse, ChatMessage.MessageRole.ASSISTANT);
        messageRepository.save(assistantMessage);
        
        conversation.updateTimestamp();
        conversationRepository.save(conversation);
        
        return ChatResponse.builder()
                .response(responseText)
                .conversationId(conversation.getConversationId())
                .build();
    }

    public List<ChatMessageDto> getChatHistory(User user) {
        // 유저의 conversation 찾기
        ChatConversation conversation = conversationRepository.findByUser(user)
                .orElse(null);
        
        // conversation이 없으면 빈 리스트 반환
        if (conversation == null) {
            return Collections.emptyList();
        }
        
        // 이전 대화 히스토리 불러오기
        List<ChatMessage> messages = messageRepository.findByConversationOrderByCreatedAtAsc(conversation);
        
        return messages.stream()
                .map(ChatMessageDto::from)
                .collect(Collectors.toList());
    }

    private String truncateTo250Chars(String text) {
        if (text == null) {
            return "";
        }
        if (text.length() <= 250) {
            return text;
        }
        return text.substring(0, 250);
    }
}

