package org.example.cmc_backend.Repository;

import org.example.cmc_backend.Entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

}
