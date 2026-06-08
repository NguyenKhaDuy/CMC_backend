package org.example.cmc_backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillDrinkDetailDTO
{
    private Long idDrink;
    private String nameDrink;
    private String size;
    private Long price;
    private Long quality;
    private Long total;
}
