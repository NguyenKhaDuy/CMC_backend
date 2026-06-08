package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.DTO.BillDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BillService {
    Object getAllBillsByUser(String idUser);
}
