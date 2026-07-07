package org.example.cmc_backend.Models.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingRequest {
    private Integer star;
    private String comment;
    private String movieId;
    private String userId;
    private String billId;
}
