package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.DTO.FoodDTO;
import org.example.cmc_backend.Models.Request.FoodRequest;
import org.example.cmc_backend.Models.Request.FoodSizeRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface FoodService {
    Page<FoodDTO> getAllFood(Integer pageNo);
    DataResponse getAllFood();
    Object getById(Long idFood);
    MessageResponse addFood(FoodRequest foodRequest);
    MessageResponse updateFood(FoodRequest foodRequest);
    MessageResponse deleteFood(Long idFood);
    MessageResponse addSizeFood(FoodSizeRequest foodSizeRequest);
    MessageResponse updateSizeFood(FoodSizeRequest foodSizeRequest);
    MessageResponse deleteSizeFood(FoodSizeRequest foodSizeRequest);
}
