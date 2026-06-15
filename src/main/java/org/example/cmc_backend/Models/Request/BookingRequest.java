package org.example.cmc_backend.Models.Request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookingRequest {
    private String idUser;
    private Long idBranch;
    private String voucherCode;
    private String idMovie;
    private Double totalAmount;
    private Long idSchedule;
    private List<Long> idSeats;
    private List<BookingDrinkRequest> bookingDrinkRequests;
    private List<BookingFoodRequest> bookingFoodRequests;
}
