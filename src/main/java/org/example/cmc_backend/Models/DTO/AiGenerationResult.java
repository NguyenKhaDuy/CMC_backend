package org.example.cmc_backend.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AiGenerationResult {
    private String content;
    private String provider;
    private String model;
    private boolean databaseFallback;
    private Integer promptTokens;
    private Integer completionTokens;
}
