package org.example.cmc_backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "ai_chat")
public class AIChatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSession;

    @Column(name = "current_step")
    private String currentStep;

    @Column(name = "status")
    private String status;

    @Column(name = "started_at")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime startedAt;

    @Column(name = "ended_at")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime endedAt;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private ScheduleEntity scheduleEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "context_id")
    private AIBookingContextEntity aiBookingContextEntity;
}
