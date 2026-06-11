package org.example.cmc_backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class MovieDTO {
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
    private String smallImage;
    private String largeImage;
    private String trailer;
    private Long idCategory;
    private String category;
    private List<RatingDTO> ratingDTOS;
    private List<MovieActorDTO> movieActorDTOS;
    private List<ScheduleDTO> scheduleDTOS;
}
