package org.example.cmc_backend.Api;

import org.example.cmc_backend.Models.DTO.ScheduleDTO;
import org.example.cmc_backend.Models.Request.ScheduleRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ScheduleApi {
    @Autowired
    ScheduleService scheduleService;

    @GetMapping(value = "/api/admin/schedule/id-movie={idMovie}")
    public ResponseEntity<Object> getAllSchedulesByAdmin(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @PathVariable String idMovie) {
        Page<ScheduleDTO> scheduleDTOS = scheduleService.getAllSchedulesByMovie(idMovie, pageNo);
        DataPageResponse dataPageResponse = new DataPageResponse();
        dataPageResponse.setData(scheduleDTOS.getContent());
        dataPageResponse.setMessage("Success");
        dataPageResponse.setStatus(HttpStatus.OK);
        dataPageResponse.setCurrent_page(pageNo);
        dataPageResponse.setTotal_page(scheduleDTOS.getTotalPages());
        return new ResponseEntity<>(dataPageResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/schedule/id-movie={idMovie}")
    public ResponseEntity<Object> getAllSchedules(@PathVariable String idMovie) {
        List<ScheduleDTO> scheduleDTOS = scheduleService.getAllSchedulesByMovie(idMovie);
        DataResponse dataResponse = new DataResponse();
        dataResponse.setData(scheduleDTOS);
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/schedule/id-room={idRoom}")
    public ResponseEntity<Object> getAllSchedulesByRoom(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @PathVariable Long idRoom) {
        Page<ScheduleDTO> scheduleDTOS = scheduleService.getAllSchedulesByRoom(idRoom, pageNo);
        DataPageResponse dataPageResponse = new DataPageResponse();
        dataPageResponse.setData(scheduleDTOS.getContent());
        dataPageResponse.setMessage("Success");
        dataPageResponse.setStatus(HttpStatus.OK);
        dataPageResponse.setCurrent_page(pageNo);
        dataPageResponse.setTotal_page(scheduleDTOS.getTotalPages());
        return new ResponseEntity<>(dataPageResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/schedule/date={date}")
    public ResponseEntity<Object> getAllSchedulesByRoom(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @PathVariable LocalDate date) {
        Page<ScheduleDTO> scheduleDTOS = scheduleService.getAllSchedulesByDate(date, pageNo);
        DataPageResponse dataPageResponse = new DataPageResponse();
        dataPageResponse.setData(scheduleDTOS.getContent());
        dataPageResponse.setMessage("Success");
        dataPageResponse.setStatus(HttpStatus.OK);
        dataPageResponse.setCurrent_page(pageNo);
        dataPageResponse.setTotal_page(scheduleDTOS.getTotalPages());
        return new ResponseEntity<>(dataPageResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/schedule/date={date}")
    public ResponseEntity<Object> getAllSchedulesByDate(@PathVariable LocalDate date) {
        List<ScheduleDTO> scheduleDTOS = scheduleService.getAllSchedulesByDate(date);
        DataResponse dataResponse = new DataResponse();
        dataResponse.setData(scheduleDTOS);
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/schedule/id-schedule={idSchedule}")
    public ResponseEntity<Object> getAllSchedulesById(@PathVariable Long idSchedule) {
        Object result = scheduleService.getScheduleById(idSchedule);
        if (result instanceof MessageResponse) {
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/admin/schedule")
    public ResponseEntity<Object> addSchedule(@RequestBody ScheduleRequest scheduleRequest) {
        MessageResponse result = scheduleService.addSchedule(scheduleRequest);
        return new ResponseEntity<>(result, result.getStatus());
    }

    @PutMapping(value = "/api/admin/schedule")
    public ResponseEntity<Object> updateSchedule(@RequestBody ScheduleRequest scheduleRequest) {
        MessageResponse result = scheduleService.updateSchedule(scheduleRequest);
        return new ResponseEntity<>(result, result.getStatus());
    }

    @DeleteMapping(value = "/api/admin/schedule/id-schedule={idSchedule}")
    public ResponseEntity<Object> deleteSchedule(@PathVariable Long idSchedule) {
        MessageResponse result = scheduleService.deleteSchedule(idSchedule);
        return new ResponseEntity<>(result, result.getStatus());
    }
}
