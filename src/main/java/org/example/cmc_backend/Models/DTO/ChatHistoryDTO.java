package org.example.cmc_backend.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatHistoryDTO {
    private String conversationId;
    private String currentStep;
    private Integer status;
    private LocalDateTime startedAt;
    private LocalDateTime updatedAt;
    private List<ChatMessageDTO> messages;
}
