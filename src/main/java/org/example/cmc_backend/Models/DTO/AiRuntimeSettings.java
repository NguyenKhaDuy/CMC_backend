package org.example.cmc_backend.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiRuntimeSettings {
    private String provider;
    private String model;
    private String systemPrompt;
    private String apiKey;
    private String endpoint;
    private BigDecimal temperature;
    private int maxTokens;
}
