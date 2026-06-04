package org.example.cmc_backend.Repository;

import org.example.cmc_backend.Entity.ActorEntity;
import org.example.cmc_backend.Entity.ActorMovieEntity;
import org.example.cmc_backend.Entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<ActorEntity, Long> {
    List<ActorEntity> findByActorMovieEntities(List<ActorMovieEntity> actorMovieEntities);
}
