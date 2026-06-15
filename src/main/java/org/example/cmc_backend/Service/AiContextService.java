package org.example.cmc_backend.Service;

import org.example.cmc_backend.Entity.UserEntity;
import org.example.cmc_backend.Models.DTO.AiContextResult;
import org.springframework.stereotype.Service;

@Service
public interface AiContextService {
    AiContextResult buildContext(UserEntity user, String question);
}
