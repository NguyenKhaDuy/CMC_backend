package org.example.cmc_backend.Repository;

import org.example.cmc_backend.Entity.AIUsageLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AIUsageLogRepository extends JpaRepository<AIUsageLogEntity, Long> {
    @Query("""
            select count(log)
            from AIUsageLogEntity log
            where lower(log.provider) = lower(:provider)
              and log.success = true
              and log.createdAt >= :start
              and log.createdAt < :end
            """)
    long countSuccessfulProviderCallsToday(
            @Param("provider") String provider,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
            select coalesce(sum(coalesce(log.promptToken, 0) + coalesce(log.completionToken, 0)), 0)
            from AIUsageLogEntity log
            where lower(log.provider) = lower(:provider)
              and log.success = true
              and log.createdAt >= :start
              and log.createdAt < :end
            """)
    long sumSuccessfulProviderTokensToday(
            @Param("provider") String provider,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
