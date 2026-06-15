package org.example.cmc_backend.Repository;

import org.example.cmc_backend.Entity.AISettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AISettingRepository extends JpaRepository<AISettingEntity, Long> {
    Optional<AISettingEntity> findFirstByUserEntity_IdUserAndIsActiveTrueOrderByUpdatedAt(String userId);
}
