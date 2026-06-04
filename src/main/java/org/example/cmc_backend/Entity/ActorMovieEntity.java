package org.example.cmc_backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "actor_movie")
public class ActorMovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idActorMovie;

    @Column(name = "is_main")
    private boolean isMain;

    @ManyToOne
    @JoinColumn(name = "id_actor")
    private ActorEntity actorEntity;

    @ManyToOne
    @JoinColumn(name = "id_movie")
    private MovieEntity movieEntity;
}
