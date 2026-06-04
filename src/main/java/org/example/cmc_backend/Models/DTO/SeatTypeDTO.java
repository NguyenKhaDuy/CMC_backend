package org.example.cmc_backend.Models.DTO;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatTypeDTO {
    private Long idSeatType;
    private String type;
    private Float priceMultiplier;
}
