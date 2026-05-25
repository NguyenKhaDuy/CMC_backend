package org.example.cmc_backend.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "movie")
public class MovieEntity {
    @Id
    @Column(name = "id_user")
    private String idUser;
    @Column(name = "name_movie")
    private String nameMovie;
    @Column(name = "director")
    private String director;

}
