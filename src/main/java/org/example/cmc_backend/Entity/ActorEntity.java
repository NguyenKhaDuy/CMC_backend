package org.example.cmc_backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "actor")
public class ActorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idActor;

    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "actorEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, orphanRemoval = true)
    private List<ActorMovieEntity> actorMovieEntities = new ArrayList<>();
}
