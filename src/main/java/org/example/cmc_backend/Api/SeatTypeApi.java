package org.example.cmc_backend.Api;

import org.example.cmc_backend.Models.Request.SeatTypeRequest;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Service.SeatTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SeatTypeApi {
    @Autowired
    SeatTypeService seatTypeService;

    @GetMapping(value = "/api/admin/seatType")
    public ResponseEntity<Object> getAllSeatTypes() {
        DataResponse result = seatTypeService.getAllSeatTypes();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/seatType/id-seatType={idSeatType}")
    public ResponseEntity<Object> getSeatTypeById(@PathVariable Long idSeatType) {
        Object result = seatTypeService.getSeatTypeById(idSeatType);
        if (result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/admin/seatType")
    public ResponseEntity<Object> createSeatType(@RequestBody SeatTypeRequest seatTypeRequest) {
        MessageResponse result = seatTypeService.addSeatType(seatTypeRequest);
        return new ResponseEntity<>(result, result.getStatus());
    }

    @PutMapping(value = "/api/admin/seatType")
    public ResponseEntity<Object> updateSeatType(@RequestBody SeatTypeRequest seatTypeRequest) {
        MessageResponse result = seatTypeService.updateSeatType(seatTypeRequest);
        return new ResponseEntity<>(result, result.getStatus());
    }

    @DeleteMapping(value = "/api/admin/seatType/id-seatType={idSeatType}")
    public ResponseEntity<Object> deleteSeatType(@PathVariable Long idSeatType) {
        MessageResponse result = seatTypeService.deleteSeatType(idSeatType);
        return new ResponseEntity<>(result, result.getStatus());
    }
}
