package org.example.cmc_backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ai_setting")
public class AISettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAISetting;

    @Column(name = "provider")
    private String provider;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "system_prompt")
    private String systemPrompt;

    @Column(name = "temperature")
    private BigDecimal temperature;

    @Column(name = "max_token")
    private Integer maxToken;

    @Column(name = "is_active")
    private Long isActive;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}
