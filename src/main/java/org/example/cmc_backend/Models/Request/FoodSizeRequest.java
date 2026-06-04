package org.example.cmc_backend.Models.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodSizeRequest {
    private Long idFood;
    private Long idSize;
    private Long price;
}
