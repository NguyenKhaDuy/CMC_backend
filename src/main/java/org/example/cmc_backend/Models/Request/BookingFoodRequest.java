package org.example.cmc_backend.Models.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingFoodRequest {
    private Long idFood;
    private Long idSize;
    private Long quality;
}
