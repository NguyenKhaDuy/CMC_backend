package org.example.cmc_backend.Models.Request;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SizeRequest {
    private Long idSize;
    private String size;
}
