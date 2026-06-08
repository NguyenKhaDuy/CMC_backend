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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsageLog;

    @Column(name = "provider")
    private String provider;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "prompt_token")
    private String promptToken;

    @Column(name = "completion_token")
    private String completionToken;

    @Column(name = "latency_ms")
    private String latencyMs;

    @Column(name = "success")
    private String success;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

}
