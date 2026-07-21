package org.example.cmc_backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "messenger")
public class MessengerEntity {
    @Id
    @Column(name = "id_messenger", nullable = false)
    private String idMessenger;

    @Column(name = "sender_type")
    private String senderType;

    @Column(name = "message_text", columnDefinition = "LONGTEXT")
    private String messageText;

    @Column(name = "intent_deceted")
    private String intentDeceted;

    @Column(name = "extracted_data")
    private String extractedData;

    @Column(name = "confident_score")
    private Integer confidentScore;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}
