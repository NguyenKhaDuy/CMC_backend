package org.example.cmc_backend.Service;


import org.example.cmc_backend.Entity.UserEntity;
import org.example.cmc_backend.Models.DTO.AiContextResult;
import org.example.cmc_backend.Models.DTO.AiGenerationResult;
import org.example.cmc_backend.Models.DTO.AiUsageLimitDTO;

public interface AiProviderService {

    AiGenerationResult generate(UserEntity user, String question, AiContextResult context);

    AiUsageLimitDTO usageLimits(UserEntity user);

    AiUsageLimitDTO usageLimits(String provider, String model);

}
