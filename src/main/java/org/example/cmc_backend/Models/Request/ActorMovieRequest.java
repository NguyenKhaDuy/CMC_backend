package org.example.cmc_backend.Models.Request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ActorMovieRequest {
    private String id_movie;
    private boolean is_main;
}
