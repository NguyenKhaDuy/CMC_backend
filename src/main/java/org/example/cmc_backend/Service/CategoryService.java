package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.DTO.CategoryDTO;
import org.example.cmc_backend.Models.Request.UpdateCategoryRequest;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<CategoryDTO> getAllCategories(); //all
    Object getCategoryById(Long id_category); //admin
    MessageResponse addCategory(String category_name); //admin
    MessageResponse deleteCategoryById(Long id_category); //admin
    MessageResponse updateCategory(UpdateCategoryRequest updateCategoryRequest); //admin
}
