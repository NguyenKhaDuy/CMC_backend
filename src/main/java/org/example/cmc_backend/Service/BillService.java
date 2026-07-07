package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.DTO.BillDTO;
import org.example.cmc_backend.Models.Request.BookingRequest;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
public interface BillService {
    Object getAllBillsByUser(String idUser);
    Page<BillDTO> getAllBills(Integer pageNo);
    MessageResponse updateStatusBill(String idBill, String status);
    MessageResponse booking(BookingRequest bookingRequest);
}
