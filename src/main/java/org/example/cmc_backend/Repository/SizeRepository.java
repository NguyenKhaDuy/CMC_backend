package org.example.cmc_backend.Repository;

import org.example.cmc_backend.Entity.SizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRepository extends JpaRepository<SizeEntity, Long> {
}
