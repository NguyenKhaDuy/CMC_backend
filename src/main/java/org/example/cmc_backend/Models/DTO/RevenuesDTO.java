package org.example.cmc_backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.index.qual.GTENegativeOne;

import java.util.List;

@Getter
@Setter
public class RevenuesDTO {
    private Integer year;
    private List<Revenue> revenues;
}
