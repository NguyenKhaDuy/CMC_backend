package org.example.cmc_backend.Repository;

import org.example.cmc_backend.Entity.SeatTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatTypeRepository extends JpaRepository<SeatTypeEntity, Long> {
}
