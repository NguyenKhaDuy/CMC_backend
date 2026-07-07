package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Entity.BranchEntity;
import org.example.cmc_backend.Entity.RoomEntity;
import org.example.cmc_backend.Entity.ScheduleEntity;
import org.example.cmc_backend.Models.DTO.*;
import org.example.cmc_backend.Models.Request.BranchRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Repository.BranchRepository;
import org.example.cmc_backend.Service.BranchService;
import org.example.cmc_backend.Utils.ConvertByteToBase64;
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
public class BranchServiceImplement implements BranchService {

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Page<BranchDTO> getAllBranches(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        Page<BranchEntity> branchEntities = branchRepository.findAll(pageable);
        List<BranchDTO> branchDTOS = new ArrayList<>();
        for (BranchEntity branchEntity : branchEntities) {
            BranchDTO branchDTO = new BranchDTO();
            modelMapper.map(branchEntity, branchDTO);
            List<RoomDTO> roomDTOS = new ArrayList<>();
            for (RoomEntity roomEntity : branchEntity.getRoomEntities()) {
                RoomDTO roomDTO = new RoomDTO();
                modelMapper.map(roomEntity, roomDTO);

                List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
                for (ScheduleEntity scheduleEntity : roomEntity.getScheduleEntities()) {
                    ScheduleDTO scheduleDTO = new ScheduleDTO();
                    modelMapper.map(scheduleEntity, scheduleDTO);

                    MovieDTO movieDTO = new MovieDTO();
                    modelMapper.map(scheduleEntity.getMovieEntity(), movieDTO);
                    movieDTO.setSmallImage(ConvertByteToBase64.toBase64(scheduleEntity.getMovieEntity().getSmallImage()));
                    movieDTO.setLargeImage(ConvertByteToBase64.toBase64(scheduleEntity.getMovieEntity().getLargeImage()));
                    movieDTO.setIdCategory(scheduleEntity.getMovieEntity().getCategoryEntity().getIdCategory());
                    movieDTO.setCategory(scheduleEntity.getMovieEntity().getCategoryEntity().getNameCategory());
                    scheduleDTO.setMovieDTO(movieDTO);

                    scheduleDTOS.add(scheduleDTO);
                }
                roomDTO.setScheduleDTOS(scheduleDTOS);

                roomDTOS.add(roomDTO);
            }
            branchDTO.setRoomDTOS(roomDTOS);

            branchDTOS.add(branchDTO);
        }
        return new PageImpl<>(branchDTOS, branchEntities.getPageable(), branchEntities.getTotalElements());
    }

    @Override
    public DataResponse getAllBranches() {
        DataResponse dataResponse = new DataResponse();
        List<BranchDTO> branchDTOS = new ArrayList<>();
        List<BranchEntity> branchEntities = branchRepository.findAll();

        for (BranchEntity branchEntity : branchEntities) {
            BranchDTO branchDTO = new BranchDTO();
            modelMapper.map(branchEntity, branchDTO);
            List<RoomDTO> roomDTOS = new ArrayList<>();
            for (RoomEntity roomEntity : branchEntity.getRoomEntities()) {
                RoomDTO roomDTO = new RoomDTO();
                modelMapper.map(roomEntity, roomDTO);

                List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
                for (ScheduleEntity scheduleEntity : roomEntity.getScheduleEntities()) {
                    ScheduleDTO scheduleDTO = new ScheduleDTO();
                    modelMapper.map(scheduleEntity, scheduleDTO);

                    MovieDTO movieDTO = new MovieDTO();
                    modelMapper.map(scheduleEntity.getMovieEntity(), movieDTO);
                    movieDTO.setSmallImage(ConvertByteToBase64.toBase64(scheduleEntity.getMovieEntity().getSmallImage()));
                    movieDTO.setLargeImage(ConvertByteToBase64.toBase64(scheduleEntity.getMovieEntity().getLargeImage()));
                    movieDTO.setIdCategory(scheduleEntity.getMovieEntity().getCategoryEntity().getIdCategory());
                    movieDTO.setCategory(scheduleEntity.getMovieEntity().getCategoryEntity().getNameCategory());
                    scheduleDTO.setMovieDTO(movieDTO);

                    scheduleDTOS.add(scheduleDTO);
                }
                roomDTO.setScheduleDTOS(scheduleDTOS);

                roomDTOS.add(roomDTO);
            }
            branchDTO.setRoomDTOS(roomDTOS);
            branchDTOS.add(branchDTO);
        }
        dataResponse.setData(branchDTOS);
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        return dataResponse;
    }

    @Override
    public Object getBranchById(Long idBranch) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        try {
            BranchEntity branchEntity = branchRepository.findById(idBranch).get();
            List<RoomDTO> roomDTOS = new ArrayList<>();
            BranchDTO branchDTO = new BranchDTO();
            modelMapper.map(branchEntity, branchDTO);
            for (RoomEntity roomEntity : branchEntity.getRoomEntities()) {
                RoomDTO roomDTO = new RoomDTO();
                modelMapper.map(roomEntity, roomDTO);

                List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
                for (ScheduleEntity scheduleEntity : roomEntity.getScheduleEntities()) {
                    ScheduleDTO scheduleDTO = new ScheduleDTO();
                    modelMapper.map(scheduleEntity, scheduleDTO);

                    MovieDTO movieDTO = new MovieDTO();
                    modelMapper.map(scheduleEntity.getMovieEntity(), movieDTO);
                    movieDTO.setSmallImage(ConvertByteToBase64.toBase64(scheduleEntity.getMovieEntity().getSmallImage()));
                    movieDTO.setLargeImage(ConvertByteToBase64.toBase64(scheduleEntity.getMovieEntity().getLargeImage()));
                    movieDTO.setIdCategory(scheduleEntity.getMovieEntity().getCategoryEntity().getIdCategory());
                    movieDTO.setCategory(scheduleEntity.getMovieEntity().getCategoryEntity().getNameCategory());
                    scheduleDTO.setMovieDTO(movieDTO);

                    scheduleDTOS.add(scheduleDTO);
                }
                roomDTO.setScheduleDTOS(scheduleDTOS);

                roomDTOS.add(roomDTO);
            }
            branchDTO.setRoomDTOS(roomDTOS);
            dataResponse.setData(branchDTO);
            dataResponse.setMessage("Success");
            dataResponse.setStatus(HttpStatus.OK);
        }catch (NoSuchElementException ex) {
            messageResponse.setMessage("Branch not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        return dataResponse;
    }

    @Override
    public MessageResponse addBranch(BranchRequest branchRequest) {
        MessageResponse messageResponse = new MessageResponse();
        BranchEntity branchEntity = new BranchEntity();
        modelMapper.map(branchRequest, branchEntity);
        branchRepository.save(branchEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse updateBranch(BranchRequest branchRequest) {
        MessageResponse messageResponse = new MessageResponse();
        try{
            BranchEntity branchEntity = branchRepository.findById(branchRequest.getIdBranch()).get();
            modelMapper.map(branchRequest, branchEntity);
            branchRepository.save(branchEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
        }catch (NoSuchElementException ex) {
            messageResponse.setMessage("Branch not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
        }
        return messageResponse;
    }

    @Override
    public MessageResponse deleteBranch(Long idBranch) {
        MessageResponse messageResponse = new MessageResponse();
        try{
            BranchEntity branchEntity = branchRepository.findById(idBranch).get();
            branchRepository.delete(branchEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
        }catch (NoSuchElementException ex) {
            messageResponse.setMessage("Branch not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
        }
        return messageResponse;
    }
}
