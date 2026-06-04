package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.DTO.MovieDTO;
import org.example.cmc_backend.Models.Request.MovieRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MovieService {
    DataPageResponse getAllMovies(Integer pageNo);
    Object getAllMoviesByBranch(Long idBranch);
    Object getMovieById(String idMovie);
    Object getMovieByCategory(Long idCategory);
    MessageResponse addMovie(MovieRequest movieRequest);
    MessageResponse updateMovie(MovieRequest movieRequest);
    MessageResponse deleteMovie(String idMovie);
}
