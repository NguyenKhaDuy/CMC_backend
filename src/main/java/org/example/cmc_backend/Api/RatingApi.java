package org.example.cmc_backend.Api;

import org.example.cmc_backend.Models.Request.RatingRequest;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RatingApi {
    @Autowired
    RatingService ratingService;

    @DeleteMapping("/api/rating/id-rating={idRating}")
    public ResponseEntity<Object> deleteRating(@PathVariable Long idRating) {
        MessageResponse result = ratingService.deleteRating(idRating);
        return new ResponseEntity<>(result, result.getStatus());
    }

    @PostMapping("/api/rating")
    public ResponseEntity<Object> addRating(@RequestBody RatingRequest ratingRequest) {
        MessageResponse result = ratingService.createRating(ratingRequest);
        return new ResponseEntity<>(result, result.getStatus());
    }
}
