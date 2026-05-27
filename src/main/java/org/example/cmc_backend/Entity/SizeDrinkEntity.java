package org.example.cmc_backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "size_drink")
public class SizeDrinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSizeDrink;

    @Column(name = "price")
    private Long price;

    @ManyToOne
    @JoinColumn(name = "id_drink")
    private DrinkEntity drinkEntity;

    @ManyToOne
    @JoinColumn(name = "id_size")
    private SizeEntity sizeEntity;

    @OneToMany(mappedBy = "sizeDrinkEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<BillDrinkDetailEntity> billDrinkDetailEntities = new ArrayList<>();
}
