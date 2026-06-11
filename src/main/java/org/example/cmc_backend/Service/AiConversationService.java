package org.example.cmc_backend.Service;

import org.example.cmc_backend.Entity.AIChatEntity;
import org.example.cmc_backend.Entity.UserEntity;
import org.example.cmc_backend.Models.DTO.ChatHistoryDTO;
import org.example.cmc_backend.Models.DTO.ChatMessageDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AiConversationService {
    AIChatEntity findOrCreateConversation(UserEntity user, String conversationId);

    void touchConversation(String conversationId, String intent);

    ChatMessageDTO saveUserMessage(UserEntity user, String conversationId, String message, String intent);

    ChatMessageDTO saveAiMessage(UserEntity user, String conversationId, String message, String intent, int confidenceScore);

    List<ChatHistoryDTO> getHistory(UserEntity user);

    void deleteHistory(UserEntity user, String conversationId);

    List<ChatMessageDTO> recentMessages(UserEntity user);
}
