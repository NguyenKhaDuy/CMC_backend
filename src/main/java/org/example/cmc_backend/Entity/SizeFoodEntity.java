package org.example.cmc_backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "size_food")
public class SizeFoodEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSizeFood;

    @Column(name = "price")
    private Long price;

    @ManyToOne
    @JoinColumn(name = "id_size")
    private SizeEntity sizeEntity;

    @ManyToOne
    @JoinColumn(name = "id_food")
    private FoodEntity foodEntity;

    @OneToMany(mappedBy = "sizeFoodEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<BillFoodDetailEntity> billFoodDetailEntities = new ArrayList<>();
}