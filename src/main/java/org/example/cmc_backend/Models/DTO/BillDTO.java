package org.example.cmc_backend.Models.DTO;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BillDTO {
    private String idBill;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    private String createdBy;
    private Double totalAmount;
    private VoucherDTO voucherDTO;
    private TicketDTO ticketDTO;
    private String status;
    private String qr;
    private BranchDTO branchDTO;
    private ScheduleDTO scheduleDTO;
    private List<BillDrinkDetailDTO> billDrinkDetailDTOS;
    private List<BillFoodDetailDTO> billFoodDetailDTOS;
    private String customerName;
    private String phoneCustomer;
}
