package org.example.cmc_backend.Service.Implement;

import lombok.Setter;
import org.example.cmc_backend.Entity.VoucherEntity;
import org.example.cmc_backend.Models.DTO.VoucherDTO;
import org.example.cmc_backend.Models.Request.VoucherRequest;
import org.example.cmc_backend.Models.Response.DataPageResponse;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.example.cmc_backend.Repository.VoucherRepository;
import org.example.cmc_backend.Service.VoucherService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class VoucherServiceImplement implements VoucherService {
    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Page<VoucherDTO> GetAllVouchers(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        Page<VoucherEntity> voucherEntities = voucherRepository.findAll(pageable);
        List<VoucherDTO> voucherDTOS = new ArrayList<>();
        for (VoucherEntity voucherEntity : voucherEntities) {
            VoucherDTO voucherDTO = new VoucherDTO();
            modelMapper.map(voucherEntity, voucherDTO);
            voucherDTOS.add(voucherDTO);
        }
        return new PageImpl<>(voucherDTOS, voucherEntities.getPageable(), voucherEntities.getTotalElements());
    }

    @Override
    public DataResponse GetAllVouchers() {
        DataResponse dataResponse = new DataResponse();
        List<VoucherEntity> voucherEntities = voucherRepository.findAll();
        List<VoucherDTO> voucherDTOS = new ArrayList<>();
        for (VoucherEntity voucherEntity : voucherEntities) {
            VoucherDTO voucherDTO = new VoucherDTO();
            modelMapper.map(voucherEntity, voucherDTO);
            voucherDTOS.add(voucherDTO);
        }
        dataResponse.setData(voucherDTOS);
        dataResponse.setStatus(HttpStatus.OK);
        dataResponse.setMessage("Success");
        return dataResponse;
    }

    @Override
    public Object GetVoucherById(Long idVoucher) {
        DataResponse dataResponse = new DataResponse();
        MessageResponse messageResponse = new MessageResponse();
        VoucherDTO voucherDTO = new VoucherDTO();
        try {
            VoucherEntity voucherEntity = voucherRepository.findById(idVoucher).get();
            modelMapper.map(voucherEntity, dataResponse);
            dataResponse.setData(voucherDTO);
            dataResponse.setStatus(HttpStatus.OK);
            dataResponse.setMessage("Success");
        } catch (NoSuchElementException ex) {
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            messageResponse.setMessage("Voucher Not Found");
            return messageResponse;
        }
        return dataResponse;
    }

    @Override
    public MessageResponse AddVoucher(VoucherRequest voucherRequest) {
        MessageResponse messageResponse = new MessageResponse();
        VoucherEntity voucherEntity = new VoucherEntity();
        modelMapper.map(voucherRequest, voucherEntity);
        voucherRepository.save(voucherEntity);
        messageResponse.setStatus(HttpStatus.OK);
        messageResponse.setMessage("Success");
        return messageResponse;
    }

    @Override
    public MessageResponse UpdateVoucher(VoucherRequest voucherRequest) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            VoucherEntity voucherEntity = voucherRepository.findById(voucherRequest.getIdVoucher()).get();
            modelMapper.map(voucherRequest, voucherEntity);
            voucherRepository.save(voucherEntity);
            messageResponse.setStatus(HttpStatus.OK);
            messageResponse.setMessage("Success");
        } catch (NoSuchElementException ex) {
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            messageResponse.setMessage("Voucher Not Found");
        }
        return messageResponse;
    }

    @Override
    public MessageResponse DeleteVoucher(Long idVoucher) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            VoucherEntity voucherEntity = voucherRepository.findById(idVoucher).get();
            voucherRepository.delete(voucherEntity);
            messageResponse.setStatus(HttpStatus.OK);
            messageResponse.setMessage("Success");
        } catch (NoSuchElementException ex) {
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            messageResponse.setMessage("Voucher Not Found");
        }
        return messageResponse;
    }

    @Override
    public Object applyVoucher(String code) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        try {
            VoucherEntity voucherEntity = voucherRepository.findByCode(code);
            if (!LocalDate.now().isBefore(voucherEntity.getExpiration())) {
                messageResponse.setStatus(HttpStatus.CONFLICT);
                messageResponse.setMessage("Voucher Expired");
                return messageResponse;
            }
            if (!(voucherEntity.getQuality() > 0)) {
                messageResponse.setStatus(HttpStatus.CONFLICT);
                messageResponse.setMessage("Voucher Quality Exceeded");
                return messageResponse;
            }
            VoucherDTO voucherDTO = new VoucherDTO();
            modelMapper.map(voucherEntity, voucherDTO);
            dataResponse.setData(voucherDTO);
            dataResponse.setStatus(HttpStatus.OK);
            dataResponse.setMessage("Success");
            return dataResponse;
        } catch (NoSuchElementException ex) {
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            messageResponse.setMessage("Voucher Not Found");
            return messageResponse;
        }
    }
}
