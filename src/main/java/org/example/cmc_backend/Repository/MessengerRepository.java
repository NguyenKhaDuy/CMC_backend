package org.example.cmc_backend.Repository;

import org.example.cmc_backend.Entity.MessengerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessengerRepository extends JpaRepository<MessengerEntity, Long> {
    List<MessengerEntity> findByUserEntity_IdUserAndExtractedDataOrderByCreatedAtAsc(String userId, String extractedData);

    List<MessengerEntity> findTop12ByUserEntity_IdUserOrderByCreatedAtDesc(String userId);

    void deleteByUserEntity_IdUserAndExtractedData(String userId, String extractedData);
}
