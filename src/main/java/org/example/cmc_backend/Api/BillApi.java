package org.example.cmc_backend.Api;

import org.example.cmc_backend.Models.DTO.BillDTO;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillApi {
    @Autowired
    BillService billService;

    @GetMapping(value = "/api/admin/bills")
    public ResponseEntity<Object> getAllBills(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        DataPageResponse dataPageResponse = new DataPageResponse();
        Page<BillDTO> billDTOS = billService.getAllBills(pageNo);
        dataPageResponse.setTotal_page(billDTOS.getTotalPages());
        dataPageResponse.setCurrent_page(pageNo);
        dataPageResponse.setData(billDTOS.getContent());
        dataPageResponse.setMessage("success");
        dataPageResponse.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(dataPageResponse, HttpStatus.OK);
    }
}
