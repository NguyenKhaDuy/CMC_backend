package org.example.cmc_backend.Api;

import org.example.cmc_backend.Entity.UserEntity;
import org.example.cmc_backend.Models.DTO.AiUsageLimitDTO;
import org.example.cmc_backend.Models.DTO.ChatHistoryDTO;
import org.example.cmc_backend.Models.Request.ChatRequest;
import org.example.cmc_backend.Models.Response.ChatResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Repository.UserRepository;
import org.example.cmc_backend.Service.AiChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
public class AiChatApi {
    private final AiChatService aiChatService;
    private final UserRepository userRepository;

    public AiChatApi(AiChatService aiChatService, UserRepository userRepository) {
        this.aiChatService = aiChatService;
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/api/ai/chat")
    public ChatResponse chat(
            @RequestBody ChatRequest request
    ) {
        UserEntity user = userRepository.findById("UUnC91RkHXK").get();
        return aiChatService.chat(user, request);
    }

    @PostMapping(value = "/api/ai/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(

            @RequestBody ChatRequest request
    ) {
        UserEntity user = userRepository.findById("UUnC91RkHXK").get();
        return aiChatService.stream(user, request);
    }

    @GetMapping("/api/ai/chat/history")
    public List<ChatHistoryDTO> history() {
        UserEntity user = userRepository.findById("UUnC91RkHXK").get();
        return aiChatService.history(user);
    }

    @GetMapping("/api/ai/chat/limits")
    public AiUsageLimitDTO limits() {
        UserEntity user = userRepository.findById("UUnC91RkHXK").get();
        return aiChatService.usageLimits(user);
    }

    @DeleteMapping("/api/ai/chat/history/{conversationId}")
    public ResponseEntity<MessageResponse> deleteHistory(
            @PathVariable String conversationId
    ) {
        UserEntity user = userRepository.findById("UUnC91RkHXK").get();
        aiChatService.deleteHistory(user, conversationId);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Deleted History");
        messageResponse.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}
