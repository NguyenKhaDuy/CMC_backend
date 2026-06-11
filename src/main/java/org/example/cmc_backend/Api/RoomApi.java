package org.example.cmc_backend.Api;

import org.example.cmc_backend.Models.Request.RoleRequest;
import org.example.cmc_backend.Models.Request.RoomRequest;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoomApi {
    @Autowired
    RoomService roomService;

    @GetMapping(value = "/api/admin/room")
    public ResponseEntity<Object> getAllRooms() {
        DataResponse dataResponse = roomService.getAllRooms();
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/room/id-room={idRoom}")
    public ResponseEntity<Object> getRoomById(@PathVariable Long idRoom) {
        Object result = roomService.getRoomById(idRoom);
        if (result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/admin/room")
    public ResponseEntity<Object> addRoom(@RequestBody RoomRequest roomRequest) {
        MessageResponse result = roomService.addRoom(roomRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(value = "/api/admin/room")
    public ResponseEntity<Object> updateRoom(@RequestBody RoomRequest roomRequest) {
        MessageResponse result = roomService.updateRoom(roomRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/admin/room/id-room={idRoom}")
    public ResponseEntity<Object> deleteRoom(@PathVariable Long idRoom) {
        MessageResponse result = roomService.deleteRoom(idRoom);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
