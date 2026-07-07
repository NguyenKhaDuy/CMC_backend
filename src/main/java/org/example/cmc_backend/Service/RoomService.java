package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.DTO.RoomDTO;
import org.example.cmc_backend.Models.Request.RoomRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface RoomService {
    DataResponse getAllRooms();
    Page<RoomDTO> getAllRooms(Integer pageNo);
    Object getRoomById(Long idRoom);
    Object getRoomByBranch(Long idBranch);
    MessageResponse addRoom(RoomRequest roomRequest);
    MessageResponse updateRoom(RoomRequest roomRequest);
    MessageResponse deleteRoom(Long idRoom);
}
