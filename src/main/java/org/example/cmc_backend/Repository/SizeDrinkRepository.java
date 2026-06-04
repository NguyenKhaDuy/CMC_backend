package org.example.cmc_backend.Repository;

import org.example.cmc_backend.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeDrinkRepository extends JpaRepository<SizeDrinkEntity, Long> {
    SizeDrinkEntity findByDrinkEntityAndSizeEntity(DrinkEntity drinkEntity, SizeEntity sizeEntity);
}
