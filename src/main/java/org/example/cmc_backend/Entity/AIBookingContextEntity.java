package org.example.cmc_backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ai_booking_context")
public class AIBookingContextEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContext;

    @Column(name = "tiket_quantity")
    private Long tiketQuantity;

    @Column(name = "selected_seat")
    private String selectedSeat;

    @Column(name = "estimated_total")
    private Long estimatedTotal;

    @Column(name = "promotion_code")
    private String promotionCode;

    @Column(name = "booking_status")
    private String bookingStatus;

    @Column(name = "expires_at")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime expiresAt;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "aiBookingContextEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<AIChatEntity> aiChatEntities = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private MovieEntity movieEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private BranchEntity branchEntity;
}
