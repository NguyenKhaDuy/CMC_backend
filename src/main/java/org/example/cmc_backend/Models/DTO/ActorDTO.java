package org.example.cmc_backend.Models.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ActorDTO {
    private Long idActor;
    private String image;
    private String name;
    private String description;
    private List<ActorMovieDTO> actorMovieDTOS;
}
