package org.example.cmc_backend.Repository;

import org.example.cmc_backend.Entity.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<VoucherEntity, Long> {
    VoucherEntity findByCode(String voucherCode);
}
