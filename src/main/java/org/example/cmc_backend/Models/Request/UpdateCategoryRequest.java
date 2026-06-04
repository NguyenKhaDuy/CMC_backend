package org.example.cmc_backend.Models.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCategoryRequest {
    private Long id_category;
    private String name_category;
}
