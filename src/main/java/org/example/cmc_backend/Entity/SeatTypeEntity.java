package org.example.cmc_backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "seat_type")
public class SeatTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSeatType;

    @Column(name = "type")
    private String type;

    @Column(name = "price_multiplier")
    private Float priceMultiplier;

    @OneToMany(mappedBy = "seatTypeEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<SeatEntity> seatEntities = new ArrayList<>();
}
