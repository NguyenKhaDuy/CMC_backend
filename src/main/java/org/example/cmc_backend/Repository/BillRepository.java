package org.example.cmc_backend.Repository;

import org.example.cmc_backend.Entity.BillEntity;
import org.example.cmc_backend.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, String> {
    List<BillEntity> findAllByUserEntity(UserEntity userEntity);
}
