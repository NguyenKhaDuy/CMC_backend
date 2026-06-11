package org.example.cmc_backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ai_usage_log")
public class AIUsageLogEntity {
    @Id
    @Column(name = "id_usage_log", nullable = false)
    private String idUsageLog;

    @Column(name = "provider")
    private String provider;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "prompt_token")
    private Integer promptToken;

    @Column(name = "completion_token")
    private Integer completionToken;

    @Column(name = "latency_ms")
    private Integer latencyMs;

    @Column(name = "success")
    private boolean success;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

}
