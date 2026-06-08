package org.example.cmc_backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bill_food_detail")
public class BillFoodDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBillFoodDetail;

    @Column(name = "quality")
    private Long quality;

    @Column(name = "total")
    private Long total;

    @ManyToOne
    @JoinColumn(name = "id_bill")
    private BillEntity billEntity;

    @ManyToOne
    @JoinColumn(name = "id_size_food")
    private SizeFoodEntity sizeFoodEntity;
}
