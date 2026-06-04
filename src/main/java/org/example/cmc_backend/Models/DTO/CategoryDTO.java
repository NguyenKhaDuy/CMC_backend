package org.example.cmc_backend.Models.DTO;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {
    private int idCategory;
    private String name_category;
}
