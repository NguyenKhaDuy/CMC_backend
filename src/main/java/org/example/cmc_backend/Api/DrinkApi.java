package org.example.cmc_backend.Api;

import org.example.cmc_backend.Models.DTO.DrinkDTO;
import org.example.cmc_backend.Models.Request.DrinkRequest;
import org.example.cmc_backend.Models.Request.DrinkSizeRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Service.DrinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DrinkApi {
    @Autowired
    DrinkService drinkService;

    @GetMapping(value = "/api/admin/drink")
    public ResponseEntity<Object> getAllDrinkAdmin(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        DataPageResponse dataPageResponse = new DataPageResponse();
        Page<DrinkDTO> drinkDTOS = drinkService.getAllDrink(pageNo);
        dataPageResponse.setMessage("success");
        dataPageResponse.setData(drinkDTOS.getContent());
        dataPageResponse.setTotal_page(drinkDTOS.getTotalPages());
        dataPageResponse.setCurrent_page(pageNo);
        dataPageResponse.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(dataPageResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/drink")
    public ResponseEntity<Object> getAllDrink() {
        DataResponse result = drinkService.getAllDrink();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/drink/id-drink={idDrink}")
    public ResponseEntity<Object> getDrinkById(@PathVariable("idDrink") Long idDrink) {
        Object result = drinkService.getById(idDrink);
        if (result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/admin/drink")
    public ResponseEntity<Object> addDrink(@RequestBody DrinkRequest drinkRequest) {
        MessageResponse result = drinkService.addDrink(drinkRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(value = "/api/admin/drink")
    public ResponseEntity<Object> updateDrink(@RequestBody DrinkRequest drinkRequest) {
        MessageResponse result = drinkService.updateDrink(drinkRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/admin/drink/size")
    public ResponseEntity<Object> addDrinkSize(@RequestBody DrinkSizeRequest drinkSizeRequest) {
        MessageResponse result = drinkService.addSizeDrink(drinkSizeRequest);
        return new ResponseEntity<>(result, result.getStatus());
    }

    @PutMapping(value = "/api/admin/drink/size")
    public ResponseEntity<Object> updateDrinkSize(@RequestBody DrinkSizeRequest drinkSizeRequest) {
        MessageResponse result = drinkService.updateSizeDrink(drinkSizeRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/admin/drink/size")
    public ResponseEntity<Object> deleteDrinkSize(@RequestBody DrinkSizeRequest drinkSizeRequest) {
        MessageResponse result = drinkService.deleteSizeDrink(drinkSizeRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
