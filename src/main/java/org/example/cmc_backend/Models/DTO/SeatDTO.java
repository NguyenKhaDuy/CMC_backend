package org.example.cmc_backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatDTO {
    private Long idSeat;
    private String name;
    private String status;
    private SeatTypeDTO seatTypeDTO;
    private Long idRoom;
    private String nameRoom;
}
