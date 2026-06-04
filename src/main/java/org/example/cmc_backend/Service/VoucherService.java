package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.DTO.VoucherDTO;
import org.example.cmc_backend.Models.Request.VoucherRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VoucherService {
    DataPageResponse GetAllVouchers(Integer pageNo);
    DataResponse GetAllVouchers();
    Object GetVoucherById(Long idVoucher);
    MessageResponse AddVoucher(VoucherRequest voucherRequest);
    MessageResponse UpdateVoucher(VoucherRequest voucherRequest);
    MessageResponse DeleteVoucher(Long idVoucher);
}
