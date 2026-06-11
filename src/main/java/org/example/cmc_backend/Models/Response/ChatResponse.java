package org.example.cmc_backend.Models.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.cmc_backend.Models.DTO.AiContextDTO;
import org.example.cmc_backend.Models.DTO.AiUsageLimitDTO;
import org.example.cmc_backend.Models.DTO.ChatMessageDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private String conversationId;
    private ChatMessageDTO userMessage;
    private ChatMessageDTO aiMessage;
    private String intent;
    private AiContextDTO context;
    private String provider;
    private String model;
    private boolean databaseFallback;
    private AiUsageLimitDTO usageLimits;
}
