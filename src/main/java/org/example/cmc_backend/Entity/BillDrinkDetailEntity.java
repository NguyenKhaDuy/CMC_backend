package org.example.cmc_backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bill_drink_detail")
public class BillDrinkDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBillDrinkDetail;

    @Column(name = "quality")
    private Long quality;

    @Column(name = "total")
    private Long total;

    @ManyToOne
    @JoinColumn(name = "id_bill")
    private BillEntity billEntity;

    @ManyToOne
    @JoinColumn(name = "id_size_drink")
    private SizeDrinkEntity sizeDrinkEntity;
}
