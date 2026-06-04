package org.example.cmc_backend.Repository;

import org.example.cmc_backend.Entity.DrinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrinkRepository extends JpaRepository<DrinkEntity, Long> {
}
