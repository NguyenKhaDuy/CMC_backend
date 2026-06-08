package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Entity.SeatTypeEntity;
import org.example.cmc_backend.Entity.SizeEntity;
import org.example.cmc_backend.Models.DTO.SeatTypeDTO;
import org.example.cmc_backend.Models.DTO.SizeDTO;
import org.example.cmc_backend.Models.Request.SizeRequest;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Repository.SizeRepository;
import org.example.cmc_backend.Service.SizeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SizeServiceImplement implements SizeService {
    @Autowired
    SizeRepository sizeRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public DataResponse getAllSize() {
        DataResponse dataResponse = new DataResponse();
        List<SizeEntity> sizeEntities = sizeRepository.findAll();
        List<SizeDTO> sizeDTOS = new ArrayList<>();
        for (SizeEntity sizeEntity : sizeEntities) {
            SizeDTO sizeDTO = new SizeDTO();
            modelMapper.map(sizeEntity, sizeDTO);
            sizeDTOS.add(sizeDTO);
        }
        dataResponse.setData(sizeDTOS);
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        return dataResponse;
    }

    @Override
    public Object getSizeById(Long idSize) {
        DataResponse dataResponse = new DataResponse();
        MessageResponse messageResponse = new MessageResponse();
        try{
            SizeEntity sizeEntity = sizeRepository.findById(idSize).get();
            SizeDTO sizeDTO = new SizeDTO();
            modelMapper.map(sizeEntity, sizeDTO);
            dataResponse.setData(sizeDTO);
            dataResponse.setMessage("Success");
            dataResponse.setStatus(HttpStatus.OK);
            return dataResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Size Not Found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }

    @Override
    public MessageResponse addSize(SizeRequest sizeRequest) {
        MessageResponse messageResponse = new MessageResponse();
        SizeEntity sizeEntity = new SizeEntity();
        modelMapper.map(sizeRequest, sizeEntity);
        sizeRepository.save(sizeEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse deleteSize(Long idSize) {
        MessageResponse messageResponse = new MessageResponse();
        try{
            SizeEntity sizeEntity = sizeRepository.findById(idSize).get();
            sizeRepository.delete(sizeEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
            return messageResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Size Not Found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }

    @Override
    public MessageResponse updateSize(SizeRequest sizeRequest) {
        MessageResponse messageResponse = new MessageResponse();
        try{
            SizeEntity sizeEntity = sizeRepository.findById(sizeRequest.getIdSize()).get();
            modelMapper.map(sizeRequest, sizeEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
            return messageResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Size Not Found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }
}
