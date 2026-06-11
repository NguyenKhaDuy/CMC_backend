package org.example.cmc_backend.Service;

import org.example.cmc_backend.Entity.UserEntity;
import org.example.cmc_backend.Models.DTO.AiUsageLimitDTO;
import org.example.cmc_backend.Models.DTO.ChatHistoryDTO;
import org.example.cmc_backend.Models.Request.ChatRequest;
import org.example.cmc_backend.Models.Response.ChatResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Service
public interface AiChatService {
    ChatResponse chat(UserEntity user, ChatRequest request);

    SseEmitter stream(UserEntity user, ChatRequest request);

    List<ChatHistoryDTO> history(UserEntity user);

    AiUsageLimitDTO usageLimits(UserEntity user);

    void deleteHistory(UserEntity user, String conversationId);
}
