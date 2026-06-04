package org.example.cmc_backend.Models.DTO;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatDTO {
    private Long idSeat;
    private String name;
    private String status;
    private SeatTypeDTO seatTypeDTO;
}
