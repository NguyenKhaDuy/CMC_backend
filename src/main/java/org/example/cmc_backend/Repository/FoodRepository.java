package org.example.cmc_backend.Repository;

import org.example.cmc_backend.Entity.FoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<FoodEntity, Long> {
}
