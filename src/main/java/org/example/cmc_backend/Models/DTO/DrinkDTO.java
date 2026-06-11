package org.example.cmc_backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DrinkDTO {
    private Long idDrink;
    private String name;
    private List<DrinkSizeDTO> drinkSizeDTOS;
}
