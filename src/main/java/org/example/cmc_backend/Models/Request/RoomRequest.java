package org.example.cmc_backend.Models.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomRequest {
    private Long idRoom;
    private Long capacity;
    private String name;
    private String totalArea;
    private Long idBranch;
}
