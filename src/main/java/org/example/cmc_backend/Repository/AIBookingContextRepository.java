package org.example.cmc_backend.Repository;

import org.example.cmc_backend.Entity.AIBookingContextEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AIBookingContextRepository extends JpaRepository<AIBookingContextEntity, Long> {
}
