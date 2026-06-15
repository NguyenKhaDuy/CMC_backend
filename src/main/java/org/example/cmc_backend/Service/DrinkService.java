package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.DTO.DrinkDTO;
import org.example.cmc_backend.Models.Request.DrinkRequest;
import org.example.cmc_backend.Models.Request.DrinkSizeRequest;
import org.example.cmc_backend.Models.Request.FoodRequest;
import org.example.cmc_backend.Models.Request.FoodSizeRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface DrinkService {
    Page<DrinkDTO> getAllDrink(Integer pageNo); //admin
    DataResponse getAllDrink(); //all
    Object getById(Long idDrink); //admin
    MessageResponse addDrink(DrinkRequest drinkRequest); //admin
    MessageResponse updateDrink(DrinkRequest drinkRequest); //admin
    MessageResponse deleteDrink(Long idDrink); //admin
    MessageResponse addSizeDrink(DrinkSizeRequest drinkSizeRequest); //admin
    MessageResponse updateSizeDrink(DrinkSizeRequest drinkSizeRequest); //admin
    MessageResponse deleteSizeDrink(DrinkSizeRequest drinkSizeRequest); //admin
}
