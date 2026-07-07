package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Entity.BillEntity;
import org.example.cmc_backend.Entity.MovieEntity;
import org.example.cmc_backend.Entity.UserEntity;
import org.example.cmc_backend.Models.DTO.Revenue;
import org.example.cmc_backend.Models.DTO.RevenuesDTO;
import org.example.cmc_backend.Models.DTO.StatisticalDTO;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Repository.BillRepository;
import org.example.cmc_backend.Repository.MovieRepository;
import org.example.cmc_backend.Repository.UserRepository;
import org.example.cmc_backend.Service.StatisticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticalServiceImpl implements StatisticalService {
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    BillRepository billRepository;
    @Autowired
    UserRepository userRepository;


    @Override
    public DataResponse getStatistics(Integer month) {
        DataResponse dataResponse = new DataResponse();
        StatisticalDTO statisticalDTO = new StatisticalDTO();
        Double totalRevenue = 0.0;
        Long totalBills = 0L;
        Long totalUsers = 0L;
        Long totalMovies = 0L;

        List<BillEntity> billEntities = billRepository.findAll();
        for (BillEntity billEntity : billEntities) {
            if (billEntity.getCreatedAt().getMonthValue() == month) {
                totalRevenue += billEntity.getTotalAmount();
                totalBills++;
            }
        }

        List<UserEntity> userEntities = userRepository.findAll();
        totalUsers = (long) userEntities.size();

        List<MovieEntity> movieEntities = movieRepository.findAll();
        for (MovieEntity movieEntity : movieEntities) {
            if (movieEntity.isShowing()){
                totalMovies++;
            }
        }


        statisticalDTO.setTotalRevenue(totalRevenue);
        statisticalDTO.setTotalMovies(totalMovies);
        statisticalDTO.setTotalUser(totalUsers);
        statisticalDTO.setTotalTicketsSold(totalBills);

        dataResponse.setData(statisticalDTO);
        dataResponse.setMessage("success");
        dataResponse.setStatus(HttpStatus.OK);
        return dataResponse;
    }

    @Override
    public DataResponse getRevenue(Integer year) {
        DataResponse dataResponse = new DataResponse();
        RevenuesDTO revenuesDTO = new RevenuesDTO();
        List<Revenue> revenues = new ArrayList<>();
        List<BillEntity> billEntities = billRepository.findAll();
        for (Long i = 1L; i <= 12; i++){
            Double totalRevenue = 0.0;
            Revenue revenue = new Revenue();
            revenue.setMonth(i);
            for (BillEntity billEntity : billEntities){
                if (billEntity.getCreatedAt().getMonthValue() == i && billEntity.getCreatedAt().getYear() == year){
                    totalRevenue += billEntity.getTotalAmount();
                }
            }
            revenue.setRevenue(totalRevenue);
            revenues.add(revenue);
        }

        revenuesDTO.setRevenues(revenues);
        revenuesDTO.setYear(year);

        dataResponse.setData(revenuesDTO);
        dataResponse.setMessage("success");
        dataResponse.setStatus(HttpStatus.OK);
        return dataResponse;
    }
}
