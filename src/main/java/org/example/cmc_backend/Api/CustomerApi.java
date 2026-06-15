package org.example.cmc_backend.Api;

import org.example.cmc_backend.Models.Request.BookingRequest;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerApi {
    @Autowired
    BillService billService;

    @PostMapping(value = "/api/customer/booking")
    public ResponseEntity<Object> booking(@RequestBody BookingRequest bookingRequest) {
        MessageResponse result = billService.booking(bookingRequest);
        if (result.getStatus() == HttpStatus.CREATED){
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(result, result.getStatus());
    }

    @GetMapping(value = "/api/customer/bill/id-user={idUser}")
    public ResponseEntity<Object> getBillIdUser(@PathVariable String idUser) {
        Object result = billService.getAllBillsByUser(idUser);
        if (result instanceof MessageResponse) {
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
