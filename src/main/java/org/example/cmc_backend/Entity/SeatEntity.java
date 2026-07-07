package org.example.cmc_backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "seat")
public class SeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSeat;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "id_room")
    private RoomEntity roomEntity;

    @ManyToOne
    @JoinColumn(name = "id_type")
    private SeatTypeEntity seatTypeEntity;

    @OneToMany(mappedBy = "seatEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<TicketEntity> ticketEntities = new ArrayList<>();

    @OneToMany(mappedBy = "seatEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<StatusSeatEntity> statusSeatEntities = new ArrayList<>();

}
