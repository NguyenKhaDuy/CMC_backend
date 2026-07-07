package org.example.cmc_backend.Api;

import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Service.StatisticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticalApi {
    @Autowired
    StatisticalService statisticalService;

    @GetMapping(value = "/api/admin/statistics")
    public ResponseEntity<Object> getStatistics(@RequestParam(name = "month") Integer month) {
        DataResponse dataResponse = statisticalService.getStatistics(month);
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/revenues")
    public ResponseEntity<Object> getRevenues(@RequestParam(name = "year") Integer year) {
        DataResponse dataResponse = statisticalService.getRevenue(year);
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }
}
