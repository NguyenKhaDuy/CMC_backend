package org.example.cmc_backend.Models.Request;

import lombok.Getter;
import lombok.Setter;
import org.example.cmc_backend.Models.DTO.SeatTypeDTO;

@Getter
@Setter
public class SeatRequest {
    private Long idSeat;
    private String name;
    private Long idSeatType;
    private Long idRoom;
}
