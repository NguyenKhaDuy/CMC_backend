package org.example.cmc_backend.Api;

import org.example.cmc_backend.Models.DTO.VoucherDTO;
import org.example.cmc_backend.Models.Request.VoucherRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class VoucherApi {
    @Autowired
    VoucherService voucherService;

    @GetMapping(value = "/api/admin/voucher")
    public ResponseEntity<Object> getAllVouchersAdmin(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<VoucherDTO> voucherDTOS = voucherService.GetAllVouchers(pageNo);
        DataPageResponse dataPageResponse = new DataPageResponse();
        dataPageResponse.setTotal_page(voucherDTOS.getTotalPages());
        dataPageResponse.setCurrent_page(pageNo);
        dataPageResponse.setStatus(HttpStatus.OK);
        dataPageResponse.setData(voucherDTOS.getContent());
        dataPageResponse.setMessage("Success");
        return new ResponseEntity<>(dataPageResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/voucher")
    public ResponseEntity<Object> getAllVouchers() {
        DataResponse result = voucherService.GetAllVouchers();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/api/voucher/id-voucher={idVoucher}")
    public ResponseEntity<Object> getVoucherById(@PathVariable Long idVoucher) {
        Object result = voucherService.GetVoucherById(idVoucher);
        if (result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/admin/voucher")
    public ResponseEntity<Object> addVoucher(@RequestBody VoucherRequest voucherRequest) {
        MessageResponse result = voucherService.AddVoucher(voucherRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(value = "/api/admin/voucher")
    public ResponseEntity<Object> updateVoucher(@RequestBody VoucherRequest voucherRequest) {
        MessageResponse result = voucherService.UpdateVoucher(voucherRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/admin/voucher/id-voucher={idVoucher}")
    public ResponseEntity<Object> deleteVoucher(@PathVariable Long idVoucher) {
        MessageResponse result = voucherService.DeleteVoucher(idVoucher);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
