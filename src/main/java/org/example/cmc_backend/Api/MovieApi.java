package org.example.cmc_backend.Api;

import org.example.cmc_backend.Models.DTO.MovieDTO;
import org.example.cmc_backend.Models.Request.AddActorForMovieRequest;
import org.example.cmc_backend.Models.Request.MovieRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MovieApi {
    @Autowired
    MovieService movieService;

    @GetMapping(value = "/api/admin/movie")
    public ResponseEntity<Object> getAllMoviesAdmin(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<MovieDTO> movieDTOS = movieService.getAllMovies(pageNo);
        DataPageResponse dataPageResponse = new DataPageResponse();
        dataPageResponse.setData(movieDTOS.getContent());
        dataPageResponse.setTotal_page(movieDTOS.getTotalPages());
        dataPageResponse.setStatus(HttpStatus.OK);
        dataPageResponse.setMessage("Success");
        dataPageResponse.setCurrent_page(pageNo);
        return new ResponseEntity<>(dataPageResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/movie/branch/id-branch={idBranch}")
    public ResponseEntity<Object> getAllMovieByBranch(@PathVariable Long idBranch){
        Object result = movieService.getAllMoviesByBranch(idBranch);
        if (result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/api/movie/id-movie={idMovie}")
    public ResponseEntity<Object> getMovieById(@PathVariable String idMovie){
        Object result = movieService.getMovieById(idMovie);
        if (result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/api/movie/category/id-category={idCategory}")
    public ResponseEntity<Object> getAllMovieByCategory(@PathVariable Long idCategory){
        Object result = movieService.getMovieByCategory(idCategory);
        if (result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/admin/movie")
    public ResponseEntity<Object> addMovie(@RequestBody MovieRequest movieRequest){
        MessageResponse result = movieService.addMovie(movieRequest);
        return new ResponseEntity<>(result, result.getStatus());
    }

    @PutMapping(value = "/api/admin/movie")
    public ResponseEntity<Object> updateMovie(@RequestBody MovieRequest movieRequest){
        MessageResponse result = movieService.updateMovie(movieRequest);
        return new ResponseEntity<>(result, result.getStatus());
    }

    @DeleteMapping(value = "/api/admin/movie/id-movie={idMovie}")
    public ResponseEntity<Object> deleteMovie(@PathVariable String idMovie){
        MessageResponse result = movieService.deleteMovie(idMovie);
        return new ResponseEntity<>(result, result.getStatus());
    }

    @PostMapping(value = "/api/admin/movie/actor")
    public ResponseEntity<Object> addActorForMovie(@RequestBody AddActorForMovieRequest addActorForMovieRequest){
        MessageResponse result = movieService.addActorMovie(addActorForMovieRequest);
        return new ResponseEntity<>(result, result.getStatus());
    }
}
