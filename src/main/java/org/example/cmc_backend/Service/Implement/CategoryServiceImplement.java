package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Entity.CategoryEntity;
import org.example.cmc_backend.Models.DTO.CategoryDTO;
import org.example.cmc_backend.Models.Request.UpdateCategoryRequest;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Repository.CategoryRepository;
import org.example.cmc_backend.Service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryServiceImplement implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<CategoryEntity> categoryEntities = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOs = new ArrayList<>();
        for (CategoryEntity categoryEntity : categoryEntities) {
            CategoryDTO categoryDTO = new CategoryDTO();
            modelMapper.map(categoryEntity, categoryDTO);
            categoryDTOs.add(categoryDTO);
        }
        return categoryDTOs;
    }

    @Override
    public DataResponse getCategoryById(Long id_category) {
        DataResponse dataResponse = new DataResponse();
        try {
            CategoryEntity categoryEntity = categoryRepository.findById(id_category).get();
            CategoryDTO categoryDTO = new CategoryDTO();
            modelMapper.map(categoryEntity, categoryDTO);
            dataResponse.setData(categoryDTO);
            dataResponse.setStatus(HttpStatus.OK);
            dataResponse.setMessage("Success");
        }catch (NoSuchElementException e) {
            dataResponse.setMessage("Category not found");
            dataResponse.setData(null);
            dataResponse.setStatus(HttpStatus.NOT_FOUND);
        }
        return dataResponse;
    }

    @Override
    public MessageResponse addCategory(String category_name) {
        MessageResponse messageResponse = new MessageResponse();
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName_category(category_name);
        categoryRepository.save(categoryEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse deleteCategoryById(Long id_category) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            CategoryEntity categoryEntity = categoryRepository.findById(id_category).get();
            categoryRepository.delete(categoryEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
        }catch (NoSuchElementException e) {
            messageResponse.setMessage("Category not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
        }
        return messageResponse;
    }

    @Override
    public MessageResponse updateCategory(UpdateCategoryRequest updateCategoryRequest) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            CategoryEntity categoryEntity = categoryRepository.findById(updateCategoryRequest.getId_category()).get();
            categoryEntity.setName_category(updateCategoryRequest.getName_category());
            categoryRepository.save(categoryEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
        }catch (NoSuchElementException e) {
            messageResponse.setMessage("Category not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
        }
        return messageResponse;
    }
}
