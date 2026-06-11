package org.example.cmc_backend.Models.Request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddActorForMovieRequest {
    private String idMovie;
    private List<AddMovieActorRequest> addMovieActorRequests;
}
