package org.example.cmc_backend.Models.DTO;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class VoucherDTO {
    private Long idVoucher;
    private String code;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate expiration;
    private Long discount;
    private Long quality;
}
