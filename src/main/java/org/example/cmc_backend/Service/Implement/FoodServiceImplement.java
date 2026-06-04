package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Entity.FoodEntity;
import org.example.cmc_backend.Entity.SizeEntity;
import org.example.cmc_backend.Entity.SizeFoodEntity;
import org.example.cmc_backend.Models.DTO.FoodDTO;
import org.example.cmc_backend.Models.DTO.FoodSizeDTO;
import org.example.cmc_backend.Models.Request.FoodRequest;
import org.example.cmc_backend.Models.Request.FoodSizeRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Repository.FoodRepository;
import org.example.cmc_backend.Repository.SizeFoodRepository;
import org.example.cmc_backend.Repository.SizeRepository;
import org.example.cmc_backend.Service.FoodService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FoodServiceImplement implements FoodService {
    @Autowired
    FoodRepository foodRepository;
    @Autowired
    SizeRepository sizeRepository;
    @Autowired
    SizeFoodRepository sizeFoodRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public DataPageResponse getAllFood(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        DataPageResponse dataPageResponse = new DataPageResponse();
        Page<FoodEntity> foodEntities = foodRepository.findAll(pageable);
        List<FoodDTO> foodDTOS = new ArrayList<>();
        List<FoodSizeDTO> foodSizeDTOS = new ArrayList<>();
        for (FoodEntity foodEntity : foodEntities) {
            FoodDTO foodDTO = new FoodDTO();
            modelMapper.map(foodEntity, foodDTO);
            for (SizeFoodEntity sizeFoodEntity : foodEntity.getSizeFoodEntities()) {
                FoodSizeDTO foodSizeDTO = new FoodSizeDTO();
                foodSizeDTO.setIdSize(sizeFoodEntity.getSizeEntity().getIdSize());
                foodSizeDTO.setSize(sizeFoodEntity.getSizeEntity().getSize());
                foodSizeDTO.setPrice(sizeFoodEntity.getPrice());
                foodSizeDTOS.add(foodSizeDTO);
            }
            foodDTO.setFoodSize(foodSizeDTOS);
            foodDTOS.add(foodDTO);
        }
        dataPageResponse.setData(foodDTOS);
        dataPageResponse.setCurrent_page(pageNo);
        dataPageResponse.setTotal_page(foodEntities.getTotalElements());
        dataPageResponse.setMessage("Success");
        dataPageResponse.setStatus(HttpStatus.OK);
        return dataPageResponse;
    }

    @Override
    public DataResponse getAllFood() {
        DataResponse dataResponse = new DataResponse();
        List<FoodEntity> foodEntities = foodRepository.findAll();
        List<FoodDTO> foodDTOS = new ArrayList<>();
        List<FoodSizeDTO> foodSizeDTOS = new ArrayList<>();
        for (FoodEntity foodEntity : foodEntities) {
            FoodDTO foodDTO = new FoodDTO();
            modelMapper.map(foodEntity, foodDTO);
            for (SizeFoodEntity sizeFoodEntity : foodEntity.getSizeFoodEntities()) {
                FoodSizeDTO foodSizeDTO = new FoodSizeDTO();
                foodSizeDTO.setIdSize(sizeFoodEntity.getSizeEntity().getIdSize());
                foodSizeDTO.setSize(sizeFoodEntity.getSizeEntity().getSize());
                foodSizeDTO.setPrice(sizeFoodEntity.getPrice());
                foodSizeDTOS.add(foodSizeDTO);
            }
            foodDTO.setFoodSize(foodSizeDTOS);
            foodDTOS.add(foodDTO);
        }
        dataResponse.setData(foodDTOS);
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        return dataResponse;
    }

    @Override
    public Object getById(Long idFood) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        FoodDTO foodDTO = new FoodDTO();
        List<FoodSizeDTO> foodSizeDTOS = new ArrayList<>();
        try{
            FoodEntity foodEntity = foodRepository.findById(idFood).get();
            modelMapper.map(foodEntity, foodDTO);
            for (SizeFoodEntity sizeFoodEntity : foodEntity.getSizeFoodEntities()) {
                FoodSizeDTO foodSizeDTO = new FoodSizeDTO();
                foodSizeDTO.setIdSize(sizeFoodEntity.getSizeEntity().getIdSize());
                foodSizeDTO.setSize(sizeFoodEntity.getSizeEntity().getSize());
                foodSizeDTO.setPrice(sizeFoodEntity.getPrice());
                foodSizeDTOS.add(foodSizeDTO);
            }
            foodDTO.setFoodSize(foodSizeDTOS);
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not find food");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        dataResponse.setData(foodDTO);
        return dataResponse;
    }

    @Override
    public MessageResponse addFood(FoodRequest foodRequest) {
         MessageResponse messageResponse = new MessageResponse();
         FoodEntity foodEntity = new FoodEntity();
         foodEntity.setName(foodRequest.getFoodName());
         foodRepository.save(foodEntity);
         messageResponse.setMessage("Success");
         messageResponse.setStatus(HttpStatus.OK);
         return messageResponse;
    }

    @Override
    public MessageResponse updateFood(FoodRequest foodRequest) {
        MessageResponse messageResponse = new MessageResponse();
        try{
            FoodEntity foodEntity = foodRepository.findById(foodRequest.getIdFood()).get();
            foodEntity.setName(foodRequest.getFoodName());
            foodRepository.save(foodEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not find food");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
        }
        return messageResponse;
    }

    @Override
    public MessageResponse deleteFood(Long idFood) {
        MessageResponse messageResponse = new MessageResponse();
        try{
            FoodEntity foodEntity = foodRepository.findById(idFood).get();
            foodRepository.delete(foodEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not find food");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
        }
        return messageResponse;
    }

    @Override
    public MessageResponse addSizeFood(FoodSizeRequest foodSizeRequest) {
        MessageResponse messageResponse = new MessageResponse();
        SizeEntity sizeEntity = null;
        FoodEntity foodEntity = null;
        try{
             foodEntity = foodRepository.findById(foodSizeRequest.getIdSize()).get();
            try{
                sizeEntity = sizeRepository.findById(foodSizeRequest.getIdSize()).get();
                SizeFoodEntity sizeFoodEntity = new SizeFoodEntity();
                sizeFoodEntity.setSizeEntity(sizeEntity);
                sizeFoodEntity.setFoodEntity(foodEntity);
                sizeFoodEntity.setPrice(foodSizeRequest.getPrice());
                sizeFoodRepository.save(sizeFoodEntity);
                messageResponse.setMessage("Success");
                messageResponse.setStatus(HttpStatus.OK);
            }catch (NoSuchElementException ex){
                messageResponse.setMessage("Can not find size");
                messageResponse.setStatus(HttpStatus.NOT_FOUND);
            }
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not find food");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
        }
        return messageResponse;
    }

    @Override
    public MessageResponse updateSizeFood(FoodSizeRequest foodSizeRequest) {
        MessageResponse messageResponse = new MessageResponse();
        SizeEntity sizeEntity = null;
        FoodEntity foodEntity = null;
        try{
            foodEntity = foodRepository.findById(foodSizeRequest.getIdFood()).get();
            try{
                sizeEntity = sizeRepository.findById(foodSizeRequest.getIdSize()).get();
                SizeFoodEntity sizeFoodEntity = sizeFoodRepository.findByFoodEntityAndSizeEntity(foodEntity, sizeEntity);
                sizeFoodEntity.setSizeEntity(sizeEntity);
                sizeFoodEntity.setPrice(foodSizeRequest.getPrice());
                sizeFoodRepository.save(sizeFoodEntity);
                messageResponse.setMessage("Success");
                messageResponse.setStatus(HttpStatus.OK);
            }catch (NoSuchElementException ex){
                messageResponse.setMessage("Can not find size");
                messageResponse.setStatus(HttpStatus.NOT_FOUND);
            }
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not find food");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
        }
        return messageResponse;
    }

    @Override
    public MessageResponse deleteSizeFood(FoodSizeRequest foodSizeRequest) {
        MessageResponse messageResponse = new MessageResponse();
        SizeEntity sizeEntity = null;
        FoodEntity foodEntity = null;
        try{
            foodEntity = foodRepository.findById(foodSizeRequest.getIdFood()).get();
            try{
                sizeEntity = sizeRepository.findById(foodSizeRequest.getIdSize()).get();
                SizeFoodEntity sizeFoodEntity = sizeFoodRepository.findByFoodEntityAndSizeEntity(foodEntity, sizeEntity);
                sizeFoodRepository.delete(sizeFoodEntity);
                messageResponse.setMessage("Success");
                messageResponse.setStatus(HttpStatus.OK);
            }catch (NoSuchElementException ex){
                messageResponse.setMessage("Can not find size");
                messageResponse.setStatus(HttpStatus.NOT_FOUND);
            }
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not find food");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
        }
        return messageResponse;
    }
}
