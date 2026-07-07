package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Entity.BranchEntity;
import org.example.cmc_backend.Entity.RoomEntity;
import org.example.cmc_backend.Entity.ScheduleEntity;
import org.example.cmc_backend.Models.DTO.BranchDTO;
import org.example.cmc_backend.Models.DTO.MovieDTO;
import org.example.cmc_backend.Models.DTO.RoomDTO;
import org.example.cmc_backend.Models.DTO.ScheduleDTO;
import org.example.cmc_backend.Models.Request.RoomRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Repository.BranchRepository;
import org.example.cmc_backend.Repository.RoomRepository;
import org.example.cmc_backend.Service.RoomService;
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
public class RoomServiceImplement implements RoomService {
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    BranchRepository branchRepository;

    @Override
    public DataResponse getAllRooms() {
        List<RoomEntity> roomEntities = roomRepository.findAll();
        List<RoomDTO> roomDTOS = new ArrayList<>();
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for (RoomEntity roomEntity : roomEntities) {
            RoomDTO roomDTO = new RoomDTO();
            modelMapper.map(roomEntity, roomDTO);
            BranchDTO branchDTO = new BranchDTO();
            modelMapper.map(roomEntity.getBranchEntity(), branchDTO);
            roomDTO.setBranchDTO(branchDTO);

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
        DataResponse dataResponse = new DataResponse();
        dataResponse.setData(roomDTOS);
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        return dataResponse;
    }

    @Override
    public Page<RoomDTO> getAllRooms(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        Page<RoomEntity> roomEntities = roomRepository.findAll(pageable);
        List<RoomDTO> roomDTOS = new ArrayList<>();
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for (RoomEntity roomEntity : roomEntities) {
            RoomDTO roomDTO = new RoomDTO();
            modelMapper.map(roomEntity, roomDTO);
            BranchDTO branchDTO = new BranchDTO();
            modelMapper.map(roomEntity.getBranchEntity(), branchDTO);
            roomDTO.setBranchDTO(branchDTO);

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
        return new PageImpl<>(roomDTOS, roomEntities.getPageable(), roomEntities.getTotalElements());
    }

    @Override
    public Object getRoomById(Long idRoom) {
        DataResponse dataResponse = new DataResponse();
        MessageResponse messageResponse = new MessageResponse();
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        try{
            RoomEntity roomEntity = roomRepository.findById(idRoom).get();
            RoomDTO roomDTO = new RoomDTO();
            modelMapper.map(roomEntity, roomDTO);
            BranchDTO branchDTO = new BranchDTO();
            modelMapper.map(roomEntity.getBranchEntity(), branchDTO);
            roomDTO.setBranchDTO(branchDTO);

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

            dataResponse.setData(roomDTO);
            dataResponse.setMessage("Success");
            dataResponse.setStatus(HttpStatus.OK);
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Room not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        return dataResponse;
    }

    @Override
    public Object getRoomByBranch(Long idBranch) {
        DataResponse dataResponse = new DataResponse();
        MessageResponse messageResponse = new MessageResponse();
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        BranchEntity branchEntity = null;
        List<RoomDTO> roomDTOS = new ArrayList<>();
        try {
            branchEntity = branchRepository.findById(idBranch).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Branch not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        try{
            List<RoomEntity> roomEntities = roomRepository.findByBranchEntity(branchEntity);
            for (RoomEntity roomEntity : roomEntities) {
                RoomDTO roomDTO = new RoomDTO();
                modelMapper.map(roomEntity, roomDTO);
                BranchDTO branchDTO = new BranchDTO();
                modelMapper.map(roomEntity.getBranchEntity(), branchDTO);
                roomDTO.setBranchDTO(branchDTO);

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
            dataResponse.setData(roomDTOS);
            dataResponse.setMessage("Success");
            dataResponse.setStatus(HttpStatus.OK);
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Room not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        return dataResponse;
    }

    @Override
    public MessageResponse addRoom(RoomRequest roomRequest) {
        MessageResponse messageResponse = new MessageResponse();
        BranchEntity branchEntity = null;
        try{
            branchEntity = branchRepository.findById(roomRequest.getIdBranch()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Branch not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        RoomEntity roomEntity = new RoomEntity();
        modelMapper.map(roomRequest, roomEntity);
        roomEntity.setBranchEntity(branchEntity);
        roomRepository.save(roomEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse updateRoom(RoomRequest roomRequest) {
        MessageResponse messageResponse = new MessageResponse();
        BranchEntity branchEntity = null;
        try{
            branchEntity = branchRepository.findById(roomRequest.getIdBranch()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Branch not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        try {
            RoomEntity roomEntity = roomRepository.findById(roomRequest.getIdRoom()).get();
            modelMapper.map(roomRequest, roomEntity);
            roomEntity.setBranchEntity(branchEntity);
            roomRepository.save(roomEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Room not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
        }
        return messageResponse;
    }

    @Override
    public MessageResponse deleteRoom(Long idRoom) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            RoomEntity roomEntity = roomRepository.findById(idRoom).get();
            roomRepository.delete(roomEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Room not found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
        }
        return messageResponse;
    }
}
