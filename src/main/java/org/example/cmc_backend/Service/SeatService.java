package org.example.cmc_backend.Service;

import org.example.cmc_backend.Entity.SeatEntity;
import org.example.cmc_backend.Models.Request.SeatRequest;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SeatService {
    DataResponse getAllSeats(); //all
    Object getAllSeatsByRoom(Long idRoom); //admin
    Object getSeatById(Long idSeat); //admin
    Object addSeat(SeatRequest seatRequest); //admin
    Object deleteSeat(Long idSeat); //admin
    Object updateSeat(SeatRequest seatRequest); //admin
    Object getAllSeatBySchedule(Long idSchedule);
}
