package org.example.cmc_backend.Api;

import org.example.cmc_backend.Models.Request.SeatTypeRequest;
import org.example.cmc_backend.Models.Request.SizeRequest;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SizeApi {
    @Autowired
    SizeService sizeService;

    @GetMapping(value = "/api/admin/size")
    public ResponseEntity<Object> getAllSize() {
        DataResponse result = sizeService.getAllSize();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/size/id-size={idSize}")
    public ResponseEntity<Object> getSizeById(@PathVariable Long idSize) {
        Object result = sizeService.getSizeById(idSize);
        if (result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/admin/size")
    public ResponseEntity<Object> createSize(@RequestBody SizeRequest sizeRequest) {
        MessageResponse result = sizeService.addSize(sizeRequest);
        return new ResponseEntity<>(result, result.getStatus());
    }

    @PutMapping(value = "/api/admin/size")
    public ResponseEntity<Object> updateSize(@RequestBody SizeRequest sizeRequest) {
        MessageResponse result = sizeService.updateSize(sizeRequest);
        return new ResponseEntity<>(result, result.getStatus());
    }

    @DeleteMapping(value = "/api/admin/size/id-size={idSize}")
    public ResponseEntity<Object> deleteSeatType(@PathVariable Long idSize) {
        MessageResponse result = sizeService.deleteSize(idSize);
        return new ResponseEntity<>(result, result.getStatus());
    }
}
