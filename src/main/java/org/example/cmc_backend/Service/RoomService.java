package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.Request.RoomRequest;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.stereotype.Service;

@Service
public interface RoomService {
    DataResponse getAllRooms();
    Object getRoomById(Long idRoom);
    MessageResponse addRoom(RoomRequest roomRequest);
    MessageResponse updateRoom(RoomRequest roomRequest);
    MessageResponse deleteRoom(Long idRoom);
}
