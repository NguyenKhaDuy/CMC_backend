package org.example.cmc_backend.Service;

import org.example.cmc_backend.Entity.SeatEntity;
import org.example.cmc_backend.Models.Request.SeatRequest;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SeatService {
    DataResponse getAllSeats();
    Object getAllSeatsByRoom(Long idRoom);
    Object getSeatById(Long idSeat);
    Object addSeat(SeatRequest seatRequest);
    Object deleteSeat(Long idSeat);
    Object updateSeat(SeatRequest seatRequest);
}
