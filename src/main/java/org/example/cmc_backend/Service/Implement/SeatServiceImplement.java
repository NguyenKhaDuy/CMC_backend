package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Entity.*;
import org.example.cmc_backend.Models.DTO.SeatDTO;
import org.example.cmc_backend.Models.DTO.SeatTypeDTO;
import org.example.cmc_backend.Models.Request.SeatRequest;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Repository.*;
import org.example.cmc_backend.Service.SeatService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SeatServiceImplement implements SeatService {
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    SeatTypeRepository seatTypeRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    StatusSeatRepository statusSeatRepository;

    @Override
    public DataResponse getAllSeats() {
        DataResponse dataResponse = new DataResponse();
        List<SeatEntity> seatEntities = seatRepository.findAll();
        List<SeatDTO> seatDTOS = new ArrayList<>();
        for (SeatEntity seatEntity : seatEntities) {
            SeatDTO seatDTO = new SeatDTO();
            modelMapper.map(seatEntity, seatDTO);
            SeatTypeDTO seatTypeDTO = new SeatTypeDTO();
            modelMapper.map(seatEntity.getSeatTypeEntity(), seatTypeDTO);
            seatDTO.setSeatTypeDTO(seatTypeDTO);
            seatDTO.setIdRoom(seatEntity.getRoomEntity().getIdRoom());
            seatDTO.setNameRoom(seatEntity.getRoomEntity().getName());
            seatDTOS.add(seatDTO);
        }
        dataResponse.setData(seatDTOS);
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        return dataResponse;
    }

    @Override
    public Object getAllSeatsByRoom(Long idRoom) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        RoomEntity roomEntity = null;
        try{
            roomEntity = roomRepository.findById(idRoom).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Room not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        List<SeatEntity> seatEntities = seatRepository.findAllByRoomEntity(roomEntity);
        List<SeatDTO> seatDTOS = new ArrayList<>();
        for (SeatEntity seatEntity : seatEntities) {
            SeatDTO seatDTO = new SeatDTO();
            modelMapper.map(seatEntity, seatDTO);
            SeatTypeDTO seatTypeDTO = new SeatTypeDTO();
            modelMapper.map(seatEntity.getSeatTypeEntity(), seatTypeDTO);
            seatDTO.setSeatTypeDTO(seatTypeDTO);
            seatDTO.setIdRoom(seatEntity.getRoomEntity().getIdRoom());
            seatDTO.setNameRoom(seatEntity.getRoomEntity().getName());
            seatDTOS.add(seatDTO);
        }
        dataResponse.setData(seatDTOS);
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        return dataResponse;
    }

    @Override
    public Object getSeatById(Long idSeat) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        try {
            SeatEntity seatEntity = seatRepository.findById(idSeat).get();
            SeatDTO seatDTO = new SeatDTO();
            modelMapper.map(seatEntity, seatDTO);
            seatDTO.setStatus("EMPTY");
            SeatTypeDTO seatTypeDTO = new SeatTypeDTO();
            modelMapper.map(seatEntity.getSeatTypeEntity(), seatTypeDTO);
            seatDTO.setSeatTypeDTO(seatTypeDTO);
            seatDTO.setIdRoom(seatEntity.getRoomEntity().getIdRoom());
            seatDTO.setNameRoom(seatEntity.getRoomEntity().getName());
            dataResponse.setData(seatDTO);
            dataResponse.setMessage("Success");
            dataResponse.setStatus(HttpStatus.OK);
            return dataResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Seat not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }

    @Override
    public Object addSeat(SeatRequest seatRequest) {
        MessageResponse messageResponse = new MessageResponse();
        SeatEntity seatEntity = new SeatEntity();
        SeatTypeEntity seatTypeEntity = null;
        RoomEntity roomEntity = null;
        try {
            seatTypeEntity = seatTypeRepository.findById(seatRequest.getIdSeatType()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Seat type not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        try{
            roomEntity = roomRepository.findById(seatRequest.getIdRoom()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Room not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        modelMapper.map(seatRequest, seatEntity);
        seatEntity.setRoomEntity(roomEntity);
        seatEntity.setSeatTypeEntity(seatTypeEntity);
        seatRepository.save(seatEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public Object deleteSeat(Long idSeat) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            SeatEntity seatEntity = seatRepository.findById(idSeat).get();
            seatRepository.delete(seatEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
            return messageResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Seat not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }

    @Override
    public Object updateSeat(SeatRequest seatRequest) {
        MessageResponse messageResponse = new MessageResponse();
        SeatTypeEntity seatTypeEntity = null;
        RoomEntity roomEntity = null;
        try{
            SeatEntity seatEntity = seatRepository.findById(seatRequest.getIdSeat()).get();
            try {
                seatTypeEntity = seatTypeRepository.findById(seatRequest.getIdSeatType()).get();
            }catch (NoSuchElementException ex){
                messageResponse.setMessage("Seat type not found");
                messageResponse.setStatus(HttpStatus.NOT_FOUND);
                return messageResponse;
            }
            try{
                roomEntity = roomRepository.findById(seatRequest.getIdRoom()).get();
            }catch (NoSuchElementException ex){
                messageResponse.setMessage("Room not found");
                messageResponse.setStatus(HttpStatus.NOT_FOUND);
                return messageResponse;
            }
            modelMapper.map(seatRequest, seatEntity);
            seatEntity.setRoomEntity(roomEntity);
            seatEntity.setSeatTypeEntity(seatTypeEntity);
            seatRepository.save(seatEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
            return messageResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Seat not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }

    @Override
    public Object getAllSeatBySchedule(Long idSchedule) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        ScheduleEntity scheduleEntity = null;
        RoomEntity roomEntity = null;
        try{
            scheduleEntity = scheduleRepository.findById(idSchedule).get();
            roomEntity = scheduleEntity.getRoomEntity();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Room not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        List<SeatEntity> seatEntities = seatRepository.findAllByRoomEntity(roomEntity);
        List<SeatDTO> seatDTOS = new ArrayList<>();
        for (SeatEntity seatEntity : seatEntities) {
            StatusSeatEntity statusSeatEntity = statusSeatRepository.findBySeatEntityAndScheduleEntity(seatEntity, scheduleEntity);
            SeatDTO seatDTO = new SeatDTO();
            modelMapper.map(seatEntity, seatDTO);
            SeatTypeDTO seatTypeDTO = new SeatTypeDTO();
            modelMapper.map(seatEntity.getSeatTypeEntity(), seatTypeDTO);
            seatDTO.setStatus(statusSeatEntity.getStatus());
            seatDTO.setSeatTypeDTO(seatTypeDTO);
            seatDTO.setIdRoom(seatEntity.getRoomEntity().getIdRoom());
            seatDTO.setNameRoom(seatEntity.getRoomEntity().getName());
            seatDTOS.add(seatDTO);
        }
        dataResponse.setData(seatDTOS);
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        return dataResponse;
    }
}
