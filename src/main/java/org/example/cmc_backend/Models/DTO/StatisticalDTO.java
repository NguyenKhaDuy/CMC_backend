package org.example.cmc_backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticalDTO {
    private Long totalTicketsSold;
    private Double totalRevenue;
    private Long totalUser;
    private Long totalMovies;
}
