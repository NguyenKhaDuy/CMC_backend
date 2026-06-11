package org.example.cmc_backend.Repository;

import org.example.cmc_backend.Entity.AIChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AiChatRepository extends JpaRepository<AIChatEntity, String> {
    Optional<AIChatEntity> findByIdSessionAndUserEntity_IdUser(String idSession, String userId);
    List<AIChatEntity> findByUserEntity_IdUserOrderByUpdatedAtDesc(String userId);
}
