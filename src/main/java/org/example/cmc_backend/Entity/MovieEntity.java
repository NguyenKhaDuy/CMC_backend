package org.example.cmc_backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "movie")
public class MovieEntity {
    @Id
    @Column(name = "id_movie")
    private String idMovie;

    @Column(name = "name_movie")
    private String nameMovie;

    @Column(name = "director")
    private String director;

    @Column(name = "duration")
    private String duration;

    @Column(name = "release_date")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate releaseDate;

    @Column(name = "language")
    private String language;

    @Column(name = "rated")
    private String rated;

    @Column(name = "is_showing")
    private boolean isShowing;

    @Column(name = "short_description")
    private String shortDescription;

    @Lob
    @Column(name = "small_image")
    private byte[] smallImage;

    @Lob
    @Column(name = "large_image")
    private byte [] largeImage;

    @Column(name = "trailer")
    private String trailer;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;

    @OneToMany(mappedBy = "movieEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    List<RatingEntity> ratingEntities = new ArrayList<>();

    @ManyToMany(mappedBy = "movieEntities", fetch = FetchType.LAZY)
    List<UserEntity> userEntities = new ArrayList<>();

    @OneToMany(mappedBy = "movieEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    List<ScheduleEntity> scheduleEntities = new ArrayList<>();

    @OneToMany(mappedBy = "movieEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    List<ActorMovieEntity> actorMovieEntities = new ArrayList<>();
}
