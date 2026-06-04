package org.example.cmc_backend.Models.Request;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class MovieRequest {
    private String idMovie;
    private String nameMovie;
    private String director;
    private String duration;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate releaseDate;
    private String language;
    private String rated;
    private boolean isShowing;
    private String shortDescription;
    private MultipartFile smallImage;
    private MultipartFile largeImage;
    private String trailer;
    private List<AddMovieActorRquest> addMovieActorRquests;
    private Long idCategory;
}
