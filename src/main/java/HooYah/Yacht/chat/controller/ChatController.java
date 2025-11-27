package HooYah.Yacht.chat.controller;

import HooYah.Yacht.chat.dto.ChatMessageDto;
import HooYah.Yacht.chat.dto.ChatRequest;
import HooYah.Yacht.chat.dto.ChatResponse;
import HooYah.Yacht.chat.service.ChatService;
import HooYah.Yacht.common.SuccessResponse;
import HooYah.Yacht.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<SuccessResponse> chat(
            @RequestBody @Valid ChatRequest request,
            @AuthenticationPrincipal User user
    ) {
        // 유저당 1개의 세션을 자동으로 관리
        ChatResponse chatResponse = chatService.sendMessage(user, request.getMessage());
        
        return ResponseEntity.ok()
                .body(new SuccessResponse(HttpStatus.OK, "success", chatResponse));
    }

    @GetMapping
    public ResponseEntity<SuccessResponse> getChatHistory(
            @AuthenticationPrincipal User user
    ) {
        // 사용자의 이전 채팅 내역 조회
        List<ChatMessageDto> chatHistory = chatService.getChatHistory(user);
        
        return ResponseEntity.ok()
                .body(new SuccessResponse(HttpStatus.OK, "success", chatHistory));
    }
}

