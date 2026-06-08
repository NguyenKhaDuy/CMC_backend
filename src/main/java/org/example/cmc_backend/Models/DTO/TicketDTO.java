package org.example.cmc_backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TicketDTO {
    private List<SeatDTO> seatDTOS;
}
