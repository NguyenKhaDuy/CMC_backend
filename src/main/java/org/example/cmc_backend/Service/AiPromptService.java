package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.DTO.AiContextResult;
import org.springframework.stereotype.Service;

@Service
public interface AiPromptService {
    String buildSystemPrompt(String customPrompt);

    String buildUserPrompt(AiContextResult context, String question);

    String ensureExternalSourceNote(String content, AiContextResult context);
}
