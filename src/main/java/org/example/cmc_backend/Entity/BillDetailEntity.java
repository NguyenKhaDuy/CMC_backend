package org.example.cmc_backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "bill_detail")
public class BillDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBillDetail;

    @Column(name = "total_price")
    private Long totalPrice;

    @ManyToOne
    @JoinColumn(name = "id_bill")
    private BillEntity billEntity;

    @OneToMany(mappedBy = "billDetailEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<BillFoodDetailEntity> billFoodDetailEntities = new ArrayList<>();

    @OneToMany(mappedBy = "billDetailEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<BillDrinkDetailEntity> billDrinkDetailEntities = new ArrayList<>();
}
