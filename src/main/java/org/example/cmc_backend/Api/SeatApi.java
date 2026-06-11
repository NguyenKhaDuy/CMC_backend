package org.example.cmc_backend.Api;

import org.example.cmc_backend.Models.Request.SeatRequest;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SeatApi {
    @Autowired
    SeatService seatService;

    @GetMapping(value = "/api/seat")
    public ResponseEntity<Object> getAllSeats() {
        DataResponse result = seatService.getAllSeats();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/seat/id-room={idRoom}")
    public ResponseEntity<Object> getAllSeatsByIdRoom(@PathVariable Long idRoom) {
        Object result = seatService.getAllSeatsByRoom(idRoom);
        if(result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/seat/id-seat={idSeat}")
    public ResponseEntity<Object> getSeatById(@PathVariable Long idSeat) {
        Object result = seatService.getSeatById(idSeat);
        if(result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/admin/seat")
    public ResponseEntity<Object> addSeat(@RequestBody SeatRequest seatRequest){
        Object result = seatService.addSeat(seatRequest);
        if(result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(value = "/api/admin/seat")
    public ResponseEntity<Object> updateSeat(@RequestBody SeatRequest seatRequest){
        Object result = seatService.updateSeat(seatRequest);
        if(result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/admin/seat/id-seat={idSeat}")
    public ResponseEntity<Object> deleteSeat(@PathVariable Long idSeat){
        Object result = seatService.deleteSeat(idSeat);
        if(result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
