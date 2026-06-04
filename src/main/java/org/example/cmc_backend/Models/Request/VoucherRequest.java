package org.example.cmc_backend.Models.Request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class VoucherRequest {
    private Long idVoucher;
    private String code;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate expiration;
    private Long discount;
    private Long quality;
}
