package org.example.cmc_backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ticket")
public class TicketEntity {
    @Id
    @Column(name = "id_ticket")
    private String idTicket;

    @Column(name = "price_ticket")
    private Double priceTicket;

    @ManyToOne
    @JoinColumn(name = "id_seat")
    private SeatEntity seatEntity;

    @ManyToOne
    @JoinColumn(name = "id_bill")
    private BillEntity billEntity;
}
