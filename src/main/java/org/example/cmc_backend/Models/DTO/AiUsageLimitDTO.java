package org.example.cmc_backend.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AiUsageLimitDTO {
    private String provider;
    private String model;
    private boolean billingGuardEnabled;
    private boolean allowPaidModels;
    private int maxOutputTokens;
    private int appDailyRequestLimit;
    private long usedRequestsToday;
    private long remainingRequestsToday;
    private int appDailyTokenLimit;
    private long usedTokensToday;
    private long remainingTokensToday;
    private int providerFreeRpm;
    private int providerFreeTpm;
    private int providerFreeRpd;
    private int googleSearchFreeRpd;
    private boolean googleSearchGroundingEnabled;
    private LocalDateTime resetAt;
    private String note;
}
