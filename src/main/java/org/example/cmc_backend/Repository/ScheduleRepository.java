package org.example.cmc_backend.Repository;

import org.example.cmc_backend.Entity.MovieEntity;
import org.example.cmc_backend.Entity.RoomEntity;
import org.example.cmc_backend.Entity.ScheduleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    Page<ScheduleEntity> findAllByMovieEntity(MovieEntity movieEntity, Pageable pageable);
    Page<ScheduleEntity> findAllByRoomEntity(RoomEntity roomEntity, Pageable pageable);
    Page<ScheduleEntity> findAllByDate(LocalDate date, Pageable pageable);
}
