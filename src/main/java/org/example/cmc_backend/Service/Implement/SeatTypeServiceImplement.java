package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Entity.SeatTypeEntity;
import org.example.cmc_backend.Models.DTO.SeatTypeDTO;
import org.example.cmc_backend.Models.Request.SeatTypeRequest;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Repository.SeatTypeRepository;
import org.example.cmc_backend.Service.SeatTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SeatTypeServiceImplement implements SeatTypeService {
    @Autowired
    SeatTypeRepository seatTypeRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public DataResponse getAllSeatTypes() {
        DataResponse dataResponse = new DataResponse();
        List<SeatTypeEntity> seatTypeEntities = seatTypeRepository.findAll();
        List<SeatTypeDTO> seatTypeDTOS = new ArrayList<>();
        for (SeatTypeEntity seatTypeEntity : seatTypeEntities) {
            SeatTypeDTO seatTypeDTO = new SeatTypeDTO();
            modelMapper.map(seatTypeEntity, seatTypeDTO);
            seatTypeDTOS.add(seatTypeDTO);
        }
        dataResponse.setData(seatTypeDTOS);
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        return dataResponse;
    }

    @Override
    public Object getSeatTypeById(Long idSeatType) {
        DataResponse dataResponse = new DataResponse();
        MessageResponse messageResponse = new MessageResponse();
        try{
            SeatTypeEntity seatTypeEntity = seatTypeRepository.findById(idSeatType).get();
            SeatTypeDTO seatTypeDTO = new SeatTypeDTO();
            modelMapper.map(seatTypeEntity, seatTypeDTO);
            dataResponse.setData(seatTypeDTO);
            dataResponse.setMessage("Success");
            dataResponse.setStatus(HttpStatus.OK);
            return dataResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Seat Type Not Found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }

    @Override
    public MessageResponse addSeatType(SeatTypeRequest seatTypeRequest) {
        MessageResponse messageResponse = new MessageResponse();
        SeatTypeEntity seatTypeEntity = new SeatTypeEntity();
        modelMapper.map(seatTypeRequest, seatTypeEntity);
        seatTypeRepository.save(seatTypeEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse deleteSeatType(Long idSeatType) {
        MessageResponse messageResponse = new MessageResponse();
        try{
            SeatTypeEntity seatTypeEntity = seatTypeRepository.findById(idSeatType).get();
            seatTypeRepository.delete(seatTypeEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
            return messageResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Seat Type Not Found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }

    @Override
    public MessageResponse updateSeatType(SeatTypeRequest seatTypeRequest) {
        MessageResponse messageResponse = new MessageResponse();
        try{
            SeatTypeEntity seatTypeEntity = seatTypeRepository.findById(seatTypeRequest.getIdSeatType()).get();
            modelMapper.map(seatTypeRequest, seatTypeEntity);
            seatTypeRepository.save(seatTypeEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
            return messageResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Seat Type Not Found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }
}
