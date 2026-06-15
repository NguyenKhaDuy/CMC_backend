package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.Request.RatingRequest;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface RatingService {
    MessageResponse deleteRating(Long idRating);
    MessageResponse createRating(RatingRequest ratingRequest);
}
