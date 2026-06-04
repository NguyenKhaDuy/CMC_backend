package org.example.cmc_backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActorMovieDTO {
    private String idMovie;
    private String nameMovie;
    private boolean is_main;
}
