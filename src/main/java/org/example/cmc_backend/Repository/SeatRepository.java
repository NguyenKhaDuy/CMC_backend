package org.example.cmc_backend.Repository;

import org.example.cmc_backend.Entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<SeatEntity, Long> {
}
