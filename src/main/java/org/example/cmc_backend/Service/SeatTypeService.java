package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.Request.SeatTypeRequest;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.stereotype.Service;

@Service
public interface SeatTypeService {
    DataResponse getAllSeatTypes();
    Object getSeatTypeById(Long idSeatType);
    MessageResponse addSeatType(SeatTypeRequest seatTypeRequest);
    MessageResponse deleteSeatType(Long idSeatType);
    MessageResponse updateSeatType(SeatTypeRequest seatTypeRequest);
}
