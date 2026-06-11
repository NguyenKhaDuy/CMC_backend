package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Entity.UserEntity;
import org.example.cmc_backend.Models.DTO.AiContextResult;
import org.example.cmc_backend.Models.DTO.AiGenerationResult;
import org.example.cmc_backend.Models.DTO.AiUsageLimitDTO;
import org.example.cmc_backend.Service.AiProviderService;
import org.springframework.stereotype.Service;

@Service
public class AiProviderServiceImplement implements AiProviderService {
    @Override
    public AiGenerationResult generate(UserEntity user, String question, AiContextResult context) {
        return null;
    }

    @Override
    public AiUsageLimitDTO usageLimits(UserEntity user) {
        return null;
    }

    @Override
    public AiUsageLimitDTO usageLimits(String provider, String model) {
        return null;
    }
}
