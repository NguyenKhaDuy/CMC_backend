package org.example.cmc_backend.Api;

import org.example.cmc_backend.Models.DTO.DrinkDTO;
import org.example.cmc_backend.Models.DTO.FoodDTO;
import org.example.cmc_backend.Models.Request.DrinkRequest;
import org.example.cmc_backend.Models.Request.DrinkSizeRequest;
import org.example.cmc_backend.Models.Request.FoodRequest;
import org.example.cmc_backend.Models.Request.FoodSizeRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FoodApi {
    @Autowired
    FoodService foodService;

    @GetMapping(value = "/api/admin/food")
    public ResponseEntity<Object> getAllFoodAdmin(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        DataPageResponse dataPageResponse = new DataPageResponse();
        Page<FoodDTO> foodDTOS = foodService.getAllFood(pageNo);
        dataPageResponse.setMessage("success");
        dataPageResponse.setData(foodDTOS.getContent());
        dataPageResponse.setTotal_page(foodDTOS.getTotalPages());
        dataPageResponse.setCurrent_page(pageNo);
        dataPageResponse.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(dataPageResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/food")
    public ResponseEntity<Object> getAllFood() {
        DataResponse result = foodService.getAllFood();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/food/id-food={idFood}")
    public ResponseEntity<Object> getDrinkById(@PathVariable Long idFood) {
        Object result = foodService.getById(idFood);
        if (result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/admin/food")
    public ResponseEntity<Object> addDrink(@RequestBody FoodRequest foodRequest) {
        MessageResponse result = foodService.addFood(foodRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(value = "/api/admin/food")
    public ResponseEntity<Object> updateFood(@RequestBody FoodRequest foodRequest) {
        MessageResponse result = foodService.updateFood(foodRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/admin/food/id-food={idFood}")
    public ResponseEntity<Object> deleteDrink(@PathVariable Long idFood) {
        MessageResponse result = foodService.deleteFood(idFood);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/admin/food/size")
    public ResponseEntity<Object> addFoodSize(@RequestBody FoodSizeRequest foodSizeRequest) {
        MessageResponse result = foodService.addSizeFood(foodSizeRequest);
        return new ResponseEntity<>(result, result.getStatus());
    }

    @PutMapping(value = "/api/admin/food/size")
    public ResponseEntity<Object> updateFoodSize(@RequestBody FoodSizeRequest foodSizeRequest) {
        MessageResponse result = foodService.updateSizeFood(foodSizeRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/admin/food/size")
    public ResponseEntity<Object> deleteFoodSize(@RequestBody FoodSizeRequest foodSizeRequest) {
        MessageResponse result = foodService.deleteSizeFood(foodSizeRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
