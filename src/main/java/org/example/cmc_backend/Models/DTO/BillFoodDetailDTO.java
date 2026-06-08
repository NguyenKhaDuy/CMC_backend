package org.example.cmc_backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillFoodDetailDTO {
    private Long idFood;
    private String nameFood;
    private String size;
    private Long price;
    private Long quality;
    private Long total;
}
