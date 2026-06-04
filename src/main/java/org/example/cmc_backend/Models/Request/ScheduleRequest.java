package org.example.cmc_backend.Models.Request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ScheduleRequest {
    private Long idSchedule;
    private Double basePrice;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime timeStart;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime timeEnd;
    private String idMovie;
    private Long idRoom;
}
