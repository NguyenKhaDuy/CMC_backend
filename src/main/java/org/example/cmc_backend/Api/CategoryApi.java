package org.example.cmc_backend.Api;

import org.example.cmc_backend.Models.DTO.CategoryDTO;
import org.example.cmc_backend.Models.Request.UpdateCategoryRequest;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryApi {
    @Autowired
    CategoryService categoryService;

    @GetMapping(value = "/api/category")
    public ResponseEntity<Object> getCategory() {
        DataResponse dataResponse = new DataResponse();
        List<CategoryDTO> categoryDTOS = categoryService.getAllCategories();
        dataResponse.setData(categoryDTOS);
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/category/id-category={idCategory}")
    public ResponseEntity<Object> getCategoryById(@PathVariable Long idCategory) {
        Object result = categoryService.getCategoryById(idCategory);
        if(result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/admin/category")
    public ResponseEntity<Object> addCategory(@RequestBody String nameCategory) {
        MessageResponse result = categoryService.addCategory(nameCategory);
        return new ResponseEntity<>(result, result.getStatus());
    }

    @PutMapping(value = "/api/admin/category")
    public ResponseEntity<Object> updateCategory(@RequestBody UpdateCategoryRequest updateCategoryRequest) {
        MessageResponse result = categoryService.updateCategory(updateCategoryRequest);
        return new ResponseEntity<>(result, result.getStatus());
    }

    @DeleteMapping(value = "/api/admin/category/id-category={idCategory}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long idCategory) {
        MessageResponse result = categoryService.deleteCategoryById(idCategory);
        return new ResponseEntity<>(result, result.getStatus());
    }
}
