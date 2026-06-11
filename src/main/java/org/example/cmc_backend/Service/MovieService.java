package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.DTO.MovieDTO;
import org.example.cmc_backend.Models.Request.ActorMovieRequest;
import org.example.cmc_backend.Models.Request.AddActorForMovieRequest;
import org.example.cmc_backend.Models.Request.MovieRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MovieService {
    Page<MovieDTO> getAllMovies(Integer pageNo); //admin
    Object getAllMoviesByBranch(Long idBranch); //all
    Object getMovieById(String idMovie); //all
    Object getMovieByCategory(Long idCategory); //all
    MessageResponse addMovie(MovieRequest movieRequest); //admin
    MessageResponse updateMovie(MovieRequest movieRequest); //admin
    MessageResponse deleteMovie(String idMovie); //admin
    MessageResponse addActorMovie(AddActorForMovieRequest addActorForMovieRequest);
}
