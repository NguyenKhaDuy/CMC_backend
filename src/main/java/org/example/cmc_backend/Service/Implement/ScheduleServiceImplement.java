package org.example.cmc_backend.Service.Implement;

import org.example.cmc_backend.Entity.*;
import org.example.cmc_backend.Models.DTO.BranchDTO;
import org.example.cmc_backend.Models.DTO.MovieDTO;
import org.example.cmc_backend.Models.DTO.RoomDTO;
import org.example.cmc_backend.Models.DTO.ScheduleDTO;
import org.example.cmc_backend.Models.Request.ScheduleRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Repository.BranchRepository;
import org.example.cmc_backend.Repository.MovieRepository;
import org.example.cmc_backend.Repository.RoomRepository;
import org.example.cmc_backend.Repository.ScheduleRepository;
import org.example.cmc_backend.Service.ScheduleService;
import org.example.cmc_backend.Utils.ConvertByteToBase64;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ScheduleServiceImplement implements ScheduleService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    RoomRepository roomRepository;

    @Override
    public Page<ScheduleDTO> getAllSchedulesByMovie(String idMovie, Integer pageNo) {
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        MovieEntity movieEntity = null;
        try {
            movieEntity = movieRepository.findById(idMovie).get();
        } catch (NoSuchElementException ex) {
            return null;
        }
        Page<ScheduleEntity> scheduleEntities = scheduleRepository.findAllByMovieEntity(movieEntity, pageable);
        for (ScheduleEntity scheduleEntity : scheduleEntities) {
            Long soldSeat = 0L;
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            modelMapper.map(scheduleEntity, scheduleDTO);

            MovieDTO movieDTO = new MovieDTO();
            modelMapper.map(scheduleEntity.getMovieEntity(), movieDTO);
            movieDTO.setSmallImage(ConvertByteToBase64.toBase64(scheduleEntity.getMovieEntity().getSmallImage()));
            movieDTO.setLargeImage(ConvertByteToBase64.toBase64(scheduleEntity.getMovieEntity().getLargeImage()));
            movieDTO.setIdCategory(scheduleEntity.getMovieEntity().getCategoryEntity().getIdCategory());
            movieDTO.setCategory(scheduleEntity.getMovieEntity().getCategoryEntity().getNameCategory());
            scheduleDTO.setMovieDTO(movieDTO);

            RoomDTO roomDTO = new RoomDTO();
            modelMapper.map(scheduleEntity.getRoomEntity(), roomDTO);
            BranchDTO branchDTO = new BranchDTO();
            modelMapper.map(scheduleEntity.getRoomEntity().getBranchEntity(), branchDTO);
            roomDTO.setBranchDTO(branchDTO);
            scheduleDTO.setRoomDTO(roomDTO);

            for (StatusSeatEntity statusSeatEntity : scheduleEntity.getStatusSeatEntities()){
                if (statusSeatEntity.getStatus().equals("BOOKED")){
                    soldSeat++;
                }
            }
            scheduleDTO.setSoldSeats(soldSeat);

            scheduleDTOS.add(scheduleDTO);
        }

        return new PageImpl<>(scheduleDTOS, scheduleEntities.getPageable(), scheduleEntities.getTotalElements());
    }

    @Override
    public List<ScheduleDTO> getAllSchedulesByMovie(String idMovie) {
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        MovieEntity movieEntity = null;
        try {
            movieEntity = movieRepository.findById(idMovie).get();
        } catch (NoSuchElementException ex) {
            return null;
        }
        List<ScheduleEntity> scheduleEntities = scheduleRepository.findAllByMovieEntity(movieEntity);
        for (ScheduleEntity scheduleEntity : scheduleEntities) {
            Long soldSeat = 0L;
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            modelMapper.map(scheduleEntity, scheduleDTO);

            MovieDTO movieDTO = new MovieDTO();
            modelMapper.map(scheduleEntity.getMovieEntity(), movieDTO);
            movieDTO.setSmallImage(ConvertByteToBase64.toBase64(scheduleEntity.getMovieEntity().getSmallImage()));
            movieDTO.setLargeImage(ConvertByteToBase64.toBase64(scheduleEntity.getMovieEntity().getLargeImage()));
            movieDTO.setIdCategory(scheduleEntity.getMovieEntity().getCategoryEntity().getIdCategory());
            movieDTO.setCategory(scheduleEntity.getMovieEntity().getCategoryEntity().getNameCategory());
            scheduleDTO.setMovieDTO(movieDTO);

            RoomDTO roomDTO = new RoomDTO();
            modelMapper.map(scheduleEntity.getRoomEntity(), roomDTO);
            BranchDTO branchDTO = new BranchDTO();
            modelMapper.map(scheduleEntity.getRoomEntity().getBranchEntity(), branchDTO);
            roomDTO.setBranchDTO(branchDTO);
            scheduleDTO.setRoomDTO(roomDTO);

            for (StatusSeatEntity statusSeatEntity : scheduleEntity.getStatusSeatEntities()){
                if (statusSeatEntity.getStatus().equals("BOOKED")){
                    soldSeat++;
                }
            }
            scheduleDTO.setSoldSeats(soldSeat);

            scheduleDTOS.add(scheduleDTO);
        }

        return scheduleDTOS;
    }

    @Override
    public Page<ScheduleDTO> getAllSchedulesByRoom(Long idRoom, Integer pageNo) {
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        RoomEntity roomEntity = null;
        try {
            roomEntity = roomRepository.findById(idRoom).get();
        } catch (NoSuchElementException ex) {
            return null;
        }
        Page<ScheduleEntity> scheduleEntities = scheduleRepository.findAllByRoomEntity(roomEntity, pageable);
        for (ScheduleEntity scheduleEntity : scheduleEntities) {
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            modelMapper.map(scheduleEntity, scheduleDTO);

            MovieDTO movieDTO = new MovieDTO();
            modelMapper.map(scheduleEntity.getMovieEntity(), movieDTO);
            movieDTO.setSmallImage(ConvertByteToBase64.toBase64(scheduleEntity.getMovieEntity().getSmallImage()));
            movieDTO.setLargeImage(ConvertByteToBase64.toBase64(scheduleEntity.getMovieEntity().getLargeImage()));
            movieDTO.setIdCategory(scheduleEntity.getMovieEntity().getCategoryEntity().getIdCategory());
            movieDTO.setCategory(scheduleEntity.getMovieEntity().getCategoryEntity().getNameCategory());
            scheduleDTO.setMovieDTO(movieDTO);

            RoomDTO roomDTO = new RoomDTO();
            modelMapper.map(scheduleEntity.getRoomEntity(), roomDTO);
            BranchDTO branchDTO = new BranchDTO();
            modelMapper.map(scheduleEntity.getRoomEntity().getBranchEntity(), branchDTO);
            roomDTO.setBranchDTO(branchDTO);
            scheduleDTO.setRoomDTO(roomDTO);

            scheduleDTOS.add(scheduleDTO);
        }
        return new PageImpl<>(scheduleDTOS, scheduleEntities.getPageable(), scheduleEntities.getTotalElements());
    }

    @Override
    public Page<ScheduleDTO> getAllSchedulesByDate(LocalDate date, Integer pageNo) {
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        Page<ScheduleEntity> scheduleEntities = scheduleRepository.findAllByDate(date, pageable);
        for (ScheduleEntity scheduleEntity : scheduleEntities) {
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            modelMapper.map(scheduleEntity, scheduleDTO);

            MovieDTO movieDTO = new MovieDTO();
            modelMapper.map(scheduleEntity.getMovieEntity(), movieDTO);
            movieDTO.setSmallImage(ConvertByteToBase64.toBase64(scheduleEntity.getMovieEntity().getSmallImage()));
            movieDTO.setLargeImage(ConvertByteToBase64.toBase64(scheduleEntity.getMovieEntity().getLargeImage()));
            movieDTO.setIdCategory(scheduleEntity.getMovieEntity().getCategoryEntity().getIdCategory());
            movieDTO.setCategory(scheduleEntity.getMovieEntity().getCategoryEntity().getNameCategory());
            scheduleDTO.setMovieDTO(movieDTO);

            RoomDTO roomDTO = new RoomDTO();
            modelMapper.map(scheduleEntity.getRoomEntity(), roomDTO);
            BranchDTO branchDTO = new BranchDTO();
            modelMapper.map(scheduleEntity.getRoomEntity().getBranchEntity(), branchDTO);
            roomDTO.setBranchDTO(branchDTO);
            scheduleDTO.setRoomDTO(roomDTO);

            scheduleDTOS.add(scheduleDTO);
        }
        return new PageImpl<>(scheduleDTOS, scheduleEntities.getPageable(), scheduleEntities.getTotalElements());
    }

    @Override
    public List<ScheduleDTO> getAllSchedulesByDate(LocalDate date) {
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        List<ScheduleEntity> scheduleEntities = scheduleRepository.findAllByDate(date);
        for (ScheduleEntity scheduleEntity : scheduleEntities) {
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            modelMapper.map(scheduleEntity, scheduleDTO);

            MovieDTO movieDTO = new MovieDTO();
            modelMapper.map(scheduleEntity.getMovieEntity(), movieDTO);
            movieDTO.setSmallImage(ConvertByteToBase64.toBase64(scheduleEntity.getMovieEntity().getSmallImage()));
            movieDTO.setLargeImage(ConvertByteToBase64.toBase64(scheduleEntity.getMovieEntity().getLargeImage()));
            movieDTO.setIdCategory(scheduleEntity.getMovieEntity().getCategoryEntity().getIdCategory());
            movieDTO.setCategory(scheduleEntity.getMovieEntity().getCategoryEntity().getNameCategory());
            scheduleDTO.setMovieDTO(movieDTO);

            RoomDTO roomDTO = new RoomDTO();
            modelMapper.map(scheduleEntity.getRoomEntity(), roomDTO);
            BranchDTO branchDTO = new BranchDTO();
            modelMapper.map(scheduleEntity.getRoomEntity().getBranchEntity(), branchDTO);
            roomDTO.setBranchDTO(branchDTO);
            scheduleDTO.setRoomDTO(roomDTO);

            scheduleDTOS.add(scheduleDTO);
        }
        return scheduleDTOS;
    }

    @Override
    public Object getScheduleById(Long idSchedule) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        try {
            ScheduleEntity scheduleEntity = scheduleRepository.findById(idSchedule).get();
            modelMapper.map(scheduleEntity, scheduleDTO);

            MovieDTO movieDTO = new MovieDTO();
            modelMapper.map(scheduleEntity.getMovieEntity(), movieDTO);
            movieDTO.setSmallImage(ConvertByteToBase64.toBase64(scheduleEntity.getMovieEntity().getSmallImage()));
            movieDTO.setLargeImage(ConvertByteToBase64.toBase64(scheduleEntity.getMovieEntity().getLargeImage()));
            movieDTO.setIdCategory(scheduleEntity.getMovieEntity().getCategoryEntity().getIdCategory());
            movieDTO.setCategory(scheduleEntity.getMovieEntity().getCategoryEntity().getNameCategory());
            scheduleDTO.setMovieDTO(movieDTO);

            RoomDTO roomDTO = new RoomDTO();
            modelMapper.map(scheduleEntity.getRoomEntity(), roomDTO);
            BranchDTO branchDTO = new BranchDTO();
            modelMapper.map(scheduleEntity.getRoomEntity().getBranchEntity(), branchDTO);
            roomDTO.setBranchDTO(branchDTO);
            scheduleDTO.setRoomDTO(roomDTO);

            dataResponse.setData(scheduleDTO);
            dataResponse.setMessage("Success");
            dataResponse.setStatus(HttpStatus.OK);
            return dataResponse;
        } catch (NoSuchElementException ex) {
            messageResponse.setMessage("Cannot find Schedule");
            messageResponse.setStatus(HttpStatus.OK);
            return messageResponse;
        }
    }

    @Override
    public MessageResponse addSchedule(ScheduleRequest scheduleRequest) {
        MessageResponse messageResponse = new MessageResponse();
        MovieEntity movieEntity = null;
        BranchEntity branchEntity = null;
        RoomEntity roomEntity = null;
        try {
            movieEntity = movieRepository.findById(scheduleRequest.getIdMovie()).get();
        }catch (NoSuchElementException e){
            messageResponse.setMessage("Cannot find Movie");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        try {
            roomEntity = roomRepository.findById(scheduleRequest.getIdRoom()).get();
        }catch (NoSuchElementException e){
            messageResponse.setMessage("Cannot find Room");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }

        //Lịch chiếu mới phải cách lịch chiếu cũ ít nhất 30p
        List<ScheduleEntity> scheduleEntities = roomEntity.getScheduleEntities();
        for (ScheduleEntity scheduleEntity : scheduleEntities) {
            if (scheduleEntity.getDate().equals(scheduleRequest.getDate())) {

                LocalTime allowedStartTime = scheduleEntity.getTimeEnd().plusMinutes(30);

                if (scheduleRequest.getTimeStart().isBefore(allowedStartTime)) {
                    messageResponse.setMessage(
                            "The scheduled screening must begin at least 30 minutes after the previous screening ends"
                    );
                    messageResponse.setStatus(HttpStatus.BAD_REQUEST);
                    return messageResponse;
                }
            }
        }

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        modelMapper.map(scheduleRequest, scheduleEntity);
        scheduleEntity.setMovieEntity(movieEntity);
        scheduleEntity.setRoomEntity(roomEntity);

        List<SeatEntity> seatEntities = roomEntity.getSeatEntities();
        List<StatusSeatEntity> statusSeatEntities = new ArrayList<>();
        for (SeatEntity seatEntity : seatEntities) {
            StatusSeatEntity statusSeatEntity = new StatusSeatEntity();
            statusSeatEntity.setStatus("EMPTY");
            statusSeatEntity.setSeatEntity(seatEntity);
            statusSeatEntity.setScheduleEntity(scheduleEntity);
            statusSeatEntities.add(statusSeatEntity);
        }
        scheduleEntity.setStatusSeatEntities(statusSeatEntities);

        scheduleRepository.save(scheduleEntity);

        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);

        return messageResponse;
    }

    @Override
    public MessageResponse updateSchedule(ScheduleRequest scheduleRequest) {
        MessageResponse messageResponse = new MessageResponse();
        MovieEntity movieEntity = null;
        BranchEntity branchEntity = null;
        RoomEntity roomEntity = null;
        try {
            movieEntity = movieRepository.findById(scheduleRequest.getIdMovie()).get();
        }catch (NoSuchElementException e){
            messageResponse.setMessage("Cannot find Movie");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        try {
            roomEntity = roomRepository.findById(scheduleRequest.getIdRoom()).get();
        }catch (NoSuchElementException e){
            messageResponse.setMessage("Cannot find Room");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        try{
            ScheduleEntity scheduleEntity = scheduleRepository.findById(scheduleRequest.getIdSchedule()).get();

            List<ScheduleEntity> scheduleEntities = roomEntity.getScheduleEntities();
            for (ScheduleEntity schedule : scheduleEntities) {
                if (schedule.getDate().equals(scheduleRequest.getDate())) {

                    LocalTime allowedStartTime = schedule.getTimeEnd().plusMinutes(30);

                    if (scheduleRequest.getTimeStart().isBefore(allowedStartTime)) {
                        messageResponse.setMessage(
                                "The scheduled screening must begin at least 30 minutes after the previous screening ends"
                        );
                        messageResponse.setStatus(HttpStatus.BAD_REQUEST);
                        return messageResponse;
                    }
                }
            }

            modelMapper.map(scheduleRequest, scheduleEntity);
            scheduleEntity.setMovieEntity(movieEntity);
            scheduleEntity.setRoomEntity(roomEntity);
            scheduleRepository.save(scheduleEntity);

            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
        }catch (NoSuchElementException e){
            messageResponse.setMessage("Cannot find Schedule");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
        }
        return messageResponse;
    }

    @Override
    public MessageResponse deleteSchedule(Long idSchedule) {
        MessageResponse messageResponse = new MessageResponse();
        try{
            ScheduleEntity scheduleEntity = scheduleRepository.findById(idSchedule).get();
            scheduleRepository.delete(scheduleEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
        }catch (NoSuchElementException e){
            messageResponse.setMessage("Cannot find Schedule");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
        }
        return messageResponse;
    }
}
