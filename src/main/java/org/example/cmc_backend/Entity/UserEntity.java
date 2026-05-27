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
@Table(name = "user")
public class UserEntity {
    @Id
    @Column(name = "id_user")
    private String idUser;

    @Column(name = "email")
    private String email;

    @Lob
    @Column(name = "avatar")
    private byte[] avatar;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password")
    private String password;

    @Column(name = "dob")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<RatingEntity> ratingEntities = new ArrayList<>();

    @ManyToMany(mappedBy = "userEntities", fetch = FetchType.LAZY)
    List<RoleEntity> roleEntities = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "movie_favorite", joinColumns = @JoinColumn(name = "id_user"), inverseJoinColumns = @JoinColumn(name = "id_movie"))
    private List<MovieEntity> movieEntities = new ArrayList<>();

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<BillEntity> billEntities = new ArrayList<>();

}
