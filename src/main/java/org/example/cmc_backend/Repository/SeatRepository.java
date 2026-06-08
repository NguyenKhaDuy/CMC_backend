package org.example.cmc_backend.Repository;

import org.example.cmc_backend.Entity.RoomEntity;
import org.example.cmc_backend.Entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<SeatEntity, Long> {
    List<SeatEntity> findAllByRoomEntity(RoomEntity roomEntity);
}
