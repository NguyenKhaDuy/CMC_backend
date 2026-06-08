package org.example.cmc_backend.Service;

import org.example.cmc_backend.Models.Request.SeatTypeRequest;
import org.example.cmc_backend.Models.Request.SizeRequest;
import org.example.cmc_backend.Models.Response.DataResponse;
import org.example.cmc_backend.Models.Response.MessageResponse;
import org.springframework.stereotype.Service;

@Service
public interface SizeService {
    DataResponse getAllSize();
    Object getSizeById(Long idSize);
    MessageResponse addSize(SizeRequest sizeRequest);
    MessageResponse deleteSize(Long idSize);
    MessageResponse updateSize(SizeRequest sizeRequest);
}
