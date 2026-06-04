package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.Request.DrinkRequest;
import org.example.cmc_backend.Models.Request.DrinkSizeRequest;
import org.example.cmc_backend.Models.Request.FoodRequest;
import org.example.cmc_backend.Models.Request.FoodSizeRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.stereotype.Service;

@Service
public interface DrinkService {
    DataPageResponse getAllDrink(Integer pageNo);
    DataResponse getAllDrink();
    Object getById(Long idDrink);
    MessageResponse addDrink(DrinkRequest drinkRequest);
    MessageResponse updateDrink(DrinkRequest drinkRequest);
    MessageResponse deleteDrink(Long idDrink);
    MessageResponse addSizeDrink(DrinkSizeRequest drinkSizeRequest);
    MessageResponse updateSizeDrink(DrinkSizeRequest drinkSizeRequest);
    MessageResponse deleteSizeDrink(DrinkSizeRequest drinkSizeRequest);
}
