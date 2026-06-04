package org.example.cmc_backend.Models.DTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoomDTO {
    private Long idRoom;
    private Long capacity;
    private String name;
    private String totalArea;
    private BranchDTO branchDTO;
    private List<ScheduleDTO> scheduleDTOS;
}
