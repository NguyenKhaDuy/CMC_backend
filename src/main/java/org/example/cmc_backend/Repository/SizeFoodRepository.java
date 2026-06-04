package org.example.cmc_backend.Repository;

import org.example.cmc_backend.Entity.FoodEntity;
import org.example.cmc_backend.Entity.SizeEntity;
import org.example.cmc_backend.Entity.SizeFoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeFoodRepository extends JpaRepository<SizeFoodEntity, Long> {
    SizeFoodEntity findByFoodEntityAndSizeEntity(FoodEntity foodEntity, SizeEntity sizeEntity);
}
