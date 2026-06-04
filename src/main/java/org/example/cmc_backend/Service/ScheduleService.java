package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.Request.ScheduleRequest;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface ScheduleService {
    Object getAllSchedulesByMovie(String idMovie, Integer pageNo);
    Object getAllSchedulesByRoom(Long idRoom, Integer pageNo);
    Object getAllSchedulesByDate(LocalDate date, Integer pageNo);
    Object getScheduleById(Long idSchedule);
    MessageResponse addSchedule(ScheduleRequest scheduleRequest);
    MessageResponse updateSchedule(ScheduleRequest scheduleRequest);
    MessageResponse deleteSchedule(Long idSchedule);
}
