package org.example.cmc_backend.Repository;

import org.example.cmc_backend.Entity.CategoryEntity;
import org.example.cmc_backend.Entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, String> {
    @Query("""
        SELECT DISTINCT m
        FROM MovieEntity m
        JOIN m.scheduleEntities s
        JOIN s.roomEntity r
        JOIN r.branchEntity b
        WHERE b.idBranch = :idBranch
    """)
    List<MovieEntity> findMoviesByBranch(@Param("idBranch") Long idBranch);

    List<MovieEntity> findMoviesByCategoryEntity(CategoryEntity categoryEntity);
}
