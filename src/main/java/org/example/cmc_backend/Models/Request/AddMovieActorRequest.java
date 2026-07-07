package org.example.cmc_backend.Models.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMovieActorRequest {
    private Long idActor;
    @JsonProperty("is_main")
    private boolean main;
}
