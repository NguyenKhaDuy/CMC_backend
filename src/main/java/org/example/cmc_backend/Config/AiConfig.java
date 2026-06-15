package org.example.cmc_backend.Config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@ConfigurationProperties(prefix = "cmc.ai")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AiConfig {
    private String provider = AiProviderRuntime.PROVIDER_GEMINI;
    private String model = "gemini-2.5-flash-lite";
    private String providerOrder = "gemini,chatgpt";
    private String apiKey = "";
    private String geminiApiKey = "";
    private String chatgptApiKey = "";
    private String geminiModel = "gemini-2.5-flash-lite";
    private String chatgptModel = "gpt-4o-mini";
    private String geminiEndpoint = "https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent";
    private String chatgptEndpoint = "https://api.openai.com/v1/chat/completions";
    private BigDecimal temperature = BigDecimal.valueOf(0.2);
    private int maxTokens = 700;
    private int timeoutSeconds = 45;
    private boolean allowPaidModels = false;
    private boolean geminiSearchGroundingEnabled = true;
    private boolean billingGuardEnabled = true;
    private int appDailyRequestLimit = 200;
    private int appDailyTokenLimit = 120000;
    private int geminiFreeRpm = 15;
    private int geminiFreeTpm = 250000;
    private int geminiFreeRpd = 1000;
    private int geminiSearchFreeRpd = 500;
}
