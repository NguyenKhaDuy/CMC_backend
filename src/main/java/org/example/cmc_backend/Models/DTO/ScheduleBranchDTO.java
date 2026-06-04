package org.example.cmc_backend.Models.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.example.cmc_backend.Entity.BranchEntity;
import org.example.cmc_backend.Entity.MovieEntity;
import org.example.cmc_backend.Entity.RoomEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ScheduleBranchDTO {
    private Long idSchedule;
    private Double basePrice;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime timeStart;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime timeEnd;
    private String movieId;
    private String nameMovie;
    private String nameRoom;
    private Long roomId;
}
