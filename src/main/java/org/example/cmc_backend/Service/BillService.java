package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.Request.BookingRequest;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.stereotype.Service;


@Service
public interface BillService {
    Object getAllBillsByUser(String idUser);
    MessageResponse booking(BookingRequest bookingRequest);
}
