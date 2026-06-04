package org.example.cmc_backend.Models.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMovieActorRquest {
    private Long idActor;
    private boolean is_main;
}
