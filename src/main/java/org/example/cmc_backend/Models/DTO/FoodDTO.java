package org.example.cmc_backend.Models.DTO;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FoodDTO {
    private Long idFood;
    private String name;
    private List<FoodSizeDTO> foodSize;
}
