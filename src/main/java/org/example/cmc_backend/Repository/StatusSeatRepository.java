package org.example.cmc_backend.Repository;

import org.example.cmc_backend.Entity.ScheduleEntity;
import org.example.cmc_backend.Entity.SeatEntity;
import org.example.cmc_backend.Entity.StatusSeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusSeatRepository extends JpaRepository<StatusSeatEntity, Long> {
    StatusSeatEntity findBySeatEntityAndScheduleEntity(SeatEntity seatEntity, ScheduleEntity scheduleEntity);
}
