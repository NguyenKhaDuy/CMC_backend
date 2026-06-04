package org.example.cmc_backend.Models.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DrinkSizeRequest {
    private Long idDrink;
    private Long idSize;
    private Long price;
}
