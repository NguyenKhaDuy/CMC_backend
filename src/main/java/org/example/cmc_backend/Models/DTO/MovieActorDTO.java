package org.example.cmc_backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieActorDTO {
    private Long idActor;
    private String image;
    private String name;
    private String description;
    private boolean is_main;
}
