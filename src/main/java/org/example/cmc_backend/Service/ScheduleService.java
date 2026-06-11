package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.DTO.ScheduleDTO;
import org.example.cmc_backend.Models.Request.ScheduleRequest;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface ScheduleService {
    Page<ScheduleDTO> getAllSchedulesByMovie(String idMovie, Integer pageNo); //admin
    List<ScheduleDTO> getAllSchedulesByMovie(String idMovie); //all
    Page<ScheduleDTO> getAllSchedulesByRoom(Long idRoom, Integer pageNo); //admin
    Page<ScheduleDTO> getAllSchedulesByDate(LocalDate date, Integer pageNo); //admin
    List<ScheduleDTO> getAllSchedulesByDate(LocalDate date); //all
    Object getScheduleById(Long idSchedule); //admin
    MessageResponse addSchedule(ScheduleRequest scheduleRequest); //admin
    MessageResponse updateSchedule(ScheduleRequest scheduleRequest); //admin
    MessageResponse deleteSchedule(Long idSchedule); //admin
}
