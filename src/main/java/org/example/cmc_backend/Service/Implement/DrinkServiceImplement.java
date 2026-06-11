package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Entity.*;
import org.example.cmc_backend.Models.DTO.DrinkDTO;
import org.example.cmc_backend.Models.DTO.DrinkSizeDTO;
import org.example.cmc_backend.Models.Request.DrinkRequest;
import org.example.cmc_backend.Models.Request.DrinkSizeRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Repository.DrinkRepository;
import org.example.cmc_backend.Repository.SizeDrinkRepository;
import org.example.cmc_backend.Repository.SizeRepository;
import org.example.cmc_backend.Service.DrinkService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DrinkServiceImplement implements DrinkService {
    @Autowired
    DrinkRepository drinkRepository;
    @Autowired
    SizeDrinkRepository sizeDrinkRepository;
    @Autowired
    SizeRepository sizeRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Page<DrinkDTO> getAllDrink(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        Page<DrinkEntity> drinkEntities = drinkRepository.findAll(pageable);
        List<DrinkDTO> drinkDTOS = new ArrayList<>();

        for (DrinkEntity drinkEntity : drinkEntities) {
            DrinkDTO drinkDTO = new DrinkDTO();
            modelMapper.map(drinkEntity, drinkDTO);
            List<DrinkSizeDTO> drinkSizeDTOS = new ArrayList<>();
            for (SizeDrinkEntity sizeDrinkEntity : drinkEntity.getSizeDrinkEntities()) {
                DrinkSizeDTO drinkSizeDTO = new DrinkSizeDTO();
                drinkSizeDTO.setIdSize(sizeDrinkEntity.getSizeEntity().getIdSize());
                drinkSizeDTO.setSize(sizeDrinkEntity.getSizeEntity().getSize());
                drinkSizeDTO.setPrice(sizeDrinkEntity.getPrice());
                drinkSizeDTOS.add(drinkSizeDTO);
            }
            drinkDTO.setDrinkSizeDTOS(drinkSizeDTOS);
            drinkDTOS.add(drinkDTO);
        }
        return new PageImpl<>(drinkDTOS, drinkEntities.getPageable(), drinkEntities.getTotalElements());
    }

    @Override
    public DataResponse getAllDrink() {
        DataResponse dataResponse = new DataResponse();
        List<DrinkEntity> drinkEntities = drinkRepository.findAll();
        List<DrinkDTO> drinkDTOS = new ArrayList<>();
        for (DrinkEntity drinkEntity : drinkEntities) {
            DrinkDTO drinkDTO = new DrinkDTO();
            modelMapper.map(drinkEntity, drinkDTO);
            List<DrinkSizeDTO> drinkSizeDTOS = new ArrayList<>();
            for (SizeDrinkEntity sizeDrinkEntity : drinkEntity.getSizeDrinkEntities()) {
                DrinkSizeDTO drinkSizeDTO = new DrinkSizeDTO();
                drinkSizeDTO.setIdSize(sizeDrinkEntity.getSizeEntity().getIdSize());
                drinkSizeDTO.setSize(sizeDrinkEntity.getSizeEntity().getSize());
                drinkSizeDTO.setPrice(sizeDrinkEntity.getPrice());
                drinkSizeDTOS.add(drinkSizeDTO);
            }
            drinkDTO.setDrinkSizeDTOS(drinkSizeDTOS);
            drinkDTOS.add(drinkDTO);
        }
        dataResponse.setData(drinkDTOS);
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        return dataResponse;
    }

    @Override
    public Object getById(Long idDrink) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        DrinkDTO drinkDTO = new DrinkDTO();
        try{
            DrinkEntity drinkEntity = drinkRepository.findById(idDrink).get();
            modelMapper.map(drinkEntity, drinkDTO);
            List<DrinkSizeDTO> drinkSizeDTOS = new ArrayList<>();
            for (SizeDrinkEntity sizeDrinkEntity : drinkEntity.getSizeDrinkEntities()) {
                DrinkSizeDTO drinkSizeDTO = new DrinkSizeDTO();
                drinkSizeDTO.setIdSize(sizeDrinkEntity.getSizeEntity().getIdSize());
                drinkSizeDTO.setSize(sizeDrinkEntity.getSizeEntity().getSize());
                drinkSizeDTO.setPrice(sizeDrinkEntity.getPrice());
                drinkSizeDTOS.add(drinkSizeDTO);
            }
            drinkDTO.setDrinkSizeDTOS(drinkSizeDTOS);
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not find drink");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        dataResponse.setData(drinkDTO);
        return dataResponse;
    }

    @Override
    public MessageResponse addDrink(DrinkRequest drinkRequest) {
        MessageResponse messageResponse = new MessageResponse();
        DrinkEntity drinkEntity = new DrinkEntity();
        drinkEntity.setName(drinkRequest.getDrinkName());
        drinkRepository.save(drinkEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse updateDrink(DrinkRequest drinkRequest) {
        MessageResponse messageResponse = new MessageResponse();
        try{
            DrinkEntity drinkEntity = drinkRepository.findById(drinkRequest.getIdDrink()).get();
            drinkEntity.setName(drinkRequest.getDrinkName());
            drinkRepository.save(drinkEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not find drink");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
        }
        return messageResponse;
    }

    @Override
    public MessageResponse deleteDrink(Long idDrink) {
        MessageResponse messageResponse = new MessageResponse();
        try{
            DrinkEntity drinkEntity = drinkRepository.findById(idDrink).get();
            drinkRepository.delete(drinkEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not find drink");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
        }
        return messageResponse;
    }

    @Override
    public MessageResponse addSizeDrink(DrinkSizeRequest drinkSizeRequest) {
        MessageResponse messageResponse = new MessageResponse();
        SizeEntity sizeEntity = null;
        DrinkEntity drinkEntity = null;
        try{
            drinkEntity = drinkRepository.findById(drinkSizeRequest.getIdSize()).get();
            try{
                sizeEntity = sizeRepository.findById(drinkSizeRequest.getIdSize()).get();
                SizeDrinkEntity sizeDrinkEntity = new SizeDrinkEntity();
                sizeDrinkEntity.setSizeEntity(sizeEntity);
                sizeDrinkEntity.setDrinkEntity(drinkEntity);
                sizeDrinkEntity.setPrice(drinkSizeRequest.getPrice());
                sizeDrinkRepository.save(sizeDrinkEntity);
                messageResponse.setMessage("Success");
                messageResponse.setStatus(HttpStatus.OK);
            }catch (NoSuchElementException ex){
                messageResponse.setMessage("Can not find size");
                messageResponse.setStatus(HttpStatus.NOT_FOUND);
            }
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not find drink");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
        }
        return messageResponse;
    }

    @Override
    public MessageResponse updateSizeDrink(DrinkSizeRequest drinkSizeRequest) {
        MessageResponse messageResponse = new MessageResponse();
        SizeEntity sizeEntity = null;
        DrinkEntity drinkEntity = null;
        try{
            drinkEntity = drinkRepository.findById(drinkSizeRequest.getIdDrink()).get();
            try{
                sizeEntity = sizeRepository.findById(drinkSizeRequest.getIdSize()).get();
                SizeDrinkEntity sizeDrinkEntity = sizeDrinkRepository.findByDrinkEntityAndSizeEntity(drinkEntity, sizeEntity);
                sizeDrinkEntity.setSizeEntity(sizeEntity);
                sizeDrinkEntity.setPrice(drinkSizeRequest.getPrice());
                sizeDrinkRepository.save(sizeDrinkEntity);
                messageResponse.setMessage("Success");
                messageResponse.setStatus(HttpStatus.OK);
            }catch (NoSuchElementException ex){
                messageResponse.setMessage("Can not find size");
                messageResponse.setStatus(HttpStatus.NOT_FOUND);
            }
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not find drink");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
        }
        return messageResponse;
    }

    @Override
    public MessageResponse deleteSizeDrink(DrinkSizeRequest drinkSizeRequest) {
        MessageResponse messageResponse = new MessageResponse();
        SizeEntity sizeEntity = null;
        DrinkEntity drinkEntity = null;
        try{
            drinkEntity = drinkRepository.findById(drinkSizeRequest.getIdDrink()).get();
            try{
                sizeEntity = sizeRepository.findById(drinkSizeRequest.getIdSize()).get();
                SizeDrinkEntity sizeDrinkEntity = sizeDrinkRepository.findByDrinkEntityAndSizeEntity(drinkEntity, sizeEntity);
                sizeDrinkRepository.delete(sizeDrinkEntity);
                messageResponse.setMessage("Success");
                messageResponse.setStatus(HttpStatus.OK);
            }catch (NoSuchElementException ex){
                messageResponse.setMessage("Can not find size");
                messageResponse.setStatus(HttpStatus.NOT_FOUND);
            }
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not find drink");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
        }
        return messageResponse;
    }
}
