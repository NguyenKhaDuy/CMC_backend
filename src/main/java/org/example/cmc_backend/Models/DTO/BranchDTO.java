package org.example.cmc_backend.Models.DTO;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BranchDTO {
    private Long idBranch;
    private String address;
    private String nameBranch;
    private String phone;
    private List<RoomDTO> roomDTOS;
}
