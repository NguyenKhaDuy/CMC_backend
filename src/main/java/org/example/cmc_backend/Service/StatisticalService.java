package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.Response.DataResponse;
import org.springframework.stereotype.Service;

@Service
public interface StatisticalService {
    DataResponse getStatistics(Integer month);
    DataResponse getRevenue(Integer year);
}
